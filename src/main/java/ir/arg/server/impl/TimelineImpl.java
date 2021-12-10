package ir.arg.server.impl;

import ir.arg.server.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TimelineImpl implements Timeline {

    private static final Database tdb = ServerSingleton.getServer().getTweetDatabase();
    private static final DateFormat df = ServerSingleton.getServer().getDateFormat();

    private boolean depleted = false;
    private final List<User> order = new ArrayList<>();
    private final Queue<User> queue = new ConcurrentLinkedQueue<>();
    private final Map<User, Integer> indices = new HashMap<>();
    private LocalDate now = LocalDate.now();
    private final int traffic;
    private Duration cutoff = Duration.ofMinutes(2);

    private static final Duration MIN_CUTOFF = Duration.ofSeconds(1);
    private static final Duration MAX_CUTOFF = Duration.from(Period.ofMonths(1));

    public TimelineImpl(@NotNull final Set<User> users) {
        assert users.size() >= 2;
        order.addAll(users);
        queue.addAll(users);
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
    private LocalDate nextTweetDate(final User user) {

        // find the index of the next tweet of this user
        final int index = indices.get(user);

        // if the user has no more tweets, return null
        if (index <= 0)
            return null;

        // find the filename of the next tweet
        final String tfn = user.getTweetAtIndex(index);

        // if that filename is null, return null
        if (tfn == null)
            return null;

        // if that file does not exist, return null
        if (!tdb.fileExists(tfn))
            return null;

        // find the actual tweet
        final Tweet tweet = TweetImpl.fromFile(tdb.readFile(tfn));

        // if that tweet is null, return null
        if (tweet == null)
            return null;

        // find the date it was sent on
        final Date d;
        try {
            d = df.parse(tweet.getSentOn());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return LocalDate.of(d, zone);
    }

    private int compareUsers(final User u1, final User u2) {
        return 0;
    }

    private void requeue() {
        if (!depleted) {
            if (queue.isEmpty()) {
                order.sort(this::compareUsers);
                int i = 0;
                Period p;
                do {
                    final User u = order.get(i);
                    p = Period.between(now, nextTweetDate(u));
                    queue.add(u);
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
            }
        }
    }

    public void discardNext() {
    }

    public JSONObject getNext() {
        if (currentQueue.isEmpty()) {

        } else {
        }
    }

    @Override
    public void discardNext(int count) {

    }

    @Override
    public JSONArray getNext(int count) {
        return null;
    }
}
