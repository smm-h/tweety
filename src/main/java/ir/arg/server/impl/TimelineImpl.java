package ir.arg.server.impl;

import ir.arg.server.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TimelineImpl implements Timeline {

    private static final Database tdb;
    private static final DateFormat df;

    static {
        final App app = App.getInstance();
        tdb = app.getTweetDatabase();
        df = app.getDateFormat();
    }

    private boolean depleted = false;
    private final List<User> list = new ArrayList<>();
    private final Queue<Tweet> queue = new ConcurrentLinkedQueue<>();
    private final Map<User, Integer> indices = new HashMap<>();
    private final int traffic;
    private Duration cutoff = Duration.ofMinutes(2);

    private static final Duration MIN_CUTOFF = Duration.ofSeconds(1);
    private static final Duration MAX_CUTOFF = Duration.ofDays(30);

    public TimelineImpl(@NotNull final Set<User> users) {
        list.addAll(users);
        for (User user : users) {
            indices.put(user, user.getTweetCount() - 1);
        }
        traffic = (int) Math.ceil(users.size() * 0.1);
    }

    public TimelineImpl(@NotNull final User user) {
        list.add(user);
        indices.put(user, user.getTweetCount() - 1);
        traffic = 1;
    }

    @Override
    public boolean isDepleted() {
        return depleted;
    }

    @Nullable
    private static Date getTweetDate(@NotNull final Tweet tweet) {

        // find the date it was sent on
        final String sentOn = tweet.getSentOn();

        // parse that to get the actual date object
        final Date d;
        try {
            d = df.parse(sentOn);
        } catch (ParseException e) {
            System.err.println("failed to parse date: " + sentOn);
            return null;
        }

        // convert that to local-date and return it
        return d;
//        return d.toInstant().atZone(zid).toLocalDate();
    }

    @Nullable
    private Tweet getNextTweetOf(@NotNull final User user) {

        // find the index of the next tweet of this user
        final int index = indices.get(user);

        // if the user has no more tweets, return null
        if (index < 0)
            return null;

        // update the index
        indices.put(user, index - 1);

        // find the filename of the next tweet
        final String tfn = user.getTweetAtIndex(index);

        // if that filename is null, return null
        if (tfn == null)
            return null;

        // if that file does not exist, return null
        if (!tdb.fileExists(tfn))
            return null;

        // find and return the actual tweet object
        try {
            return TweetImpl.fromFile(tfn);
        } catch (IOException e) {
            App.getInstance().getPaginationService().log(e);
            return null;
        }
    }

    private long getCandidateTweet(User user) {
        try {
            return Objects.requireNonNull(getTweetDate(Objects.requireNonNull(TweetImpl.fromFile(user.getTweetAtIndex(indices.get(user)))))).getTime();
        } catch (Throwable e) {
            return -1;
        }
    }

    private int compare(User u1, User u2) {
        return Long.compare(getCandidateTweet(u1), getCandidateTweet(u2));
    }

    private boolean requeue() {
        if (depleted) {
            return false;
        } else {
            if (queue.isEmpty()) {
                list.sort(this::compare);
                final User u = list.get(0);
                final Tweet t = getNextTweetOf(u);
                if (t != null) {
                    final Date d = getTweetDate(t);
                    if (d != null) {
                        queue.add(t);
                    }
                }
                if (queue.size() == 1) {
                    if (cutoff.minus(MAX_CUTOFF).isNegative())
                        cutoff = cutoff.multipliedBy(2);
                } else if (queue.size() > traffic) {
                    if (!cutoff.minus(MIN_CUTOFF).isNegative())
                        cutoff = cutoff.dividedBy(2);
                }
            } else {
                return true;
            }
            if (queue.isEmpty()) {
                depleted = true;
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void discardNext() {
        if (requeue()) {
            queue.poll();
        }
    }

    @Override
    public JSONObject getNext() {
        if (requeue()) {
            final Tweet tweet = queue.poll();
            if (tweet == null) {
                return null;
            } else {
                return tweet.serialize();
            }
        } else {
            return null;
        }
    }

    @Override
    public void discardNext(final int count) {
        for (int i = 0; i < count; i++) {
            discardNext();
        }
    }

    @Override
    public JSONArray getNext(final int count) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < count; i++) {
            JSONObject object = getNext();
            if (object != null)
                array.put(object);
        }
        return array;
    }
}
