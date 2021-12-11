package ir.arg.server.impl;

import ir.arg.server.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TimelineImpl implements Timeline {

    private static final Database tdb;
    private static final DateFormat df;
    private static final ZoneId zid;

    static {
        final Server s = ServerSingleton.getServer();
        tdb = s.getTweetDatabase();
        df = s.getDateFormat();
        zid = s.getZoneId();
    }

    private boolean depleted = false;
    private final List<User> order = new ArrayList<>();
    private final Queue<Tweet> queue = new ConcurrentLinkedQueue<>();
    private final Map<User, Integer> indices = new HashMap<>();
    private LocalDate now = LocalDate.now();
    private final int traffic;
    private Duration cutoff = Duration.ofMinutes(2);

    private static final Duration MIN_CUTOFF = Duration.ofSeconds(1);
    private static final Duration MAX_CUTOFF = Duration.from(Period.ofMonths(1));

    public TimelineImpl(@NotNull final Set<User> users) {
        assert users.size() >= 2;
        order.addAll(users);
        for (User user : users) {
            indices.put(user, user.getLastTweetIndex());
        }
        traffic = (int) Math.ceil(users.size() * 0.1);
    }

    @Override
    public boolean isDepleted() {
        return depleted;
    }

    @Nullable
    private LocalDate getTweetDate(@NotNull final Tweet tweet) {

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
        return d.toInstant().atZone(zid).toLocalDate();
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
        return TweetImpl.fromFile(tdb.readFile(tfn));
    }

    private int compareUsers(final User u1, final User u2) {
        return 0;
    }

    private boolean requeue() {
        if (depleted) {
            return false;
        } else {
            if (queue.isEmpty()) {
                order.sort(this::compareUsers);
                int i = 0;
                Duration p;
                do {
                    p = cutoff;
                    final User u = order.get(i);
                    final Tweet t = getNextTweetOf(u);
                    if (t != null) {
                        final LocalDate d = getTweetDate(t);
                        if (d != null) {
                            p = Duration.between(now, d);
                            queue.add(t);
                            now = d;
                        }
                    }
                } while (!p.minus(cutoff).isNegative());
                if (queue.size() == 1) {
                    if (cutoff.compareTo(MAX_CUTOFF) <= 0)
                        cutoff = cutoff.multipliedBy(2);
                } else if (queue.size() > traffic) {
                    if (cutoff.compareTo(MIN_CUTOFF) >= 0)
                        cutoff = cutoff.dividedBy(2);
                }
            }
            if (queue.isEmpty()) {
                depleted = true;
                return false;
            } else {
                return true;
            }
        }
    }

    public void discardNext() {
        if (requeue()) {
            queue.poll();
        }
    }

    public JSONObject getNext() {
        if (requeue()) {
            return Objects.requireNonNull(queue.poll()).serialize();
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
            if (object == null) break;
            else
                array.put(object);
        }
        return array;
    }
}
