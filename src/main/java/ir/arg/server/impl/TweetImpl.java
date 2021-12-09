package ir.arg.server.impl;

import ir.arg.server.Database;
import ir.arg.server.Server;
import ir.arg.server.Tweet;
import ir.arg.server.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class TweetImpl implements Tweet {

    private final String sender;
    private final String sentOn;
    private final String contents;
    private final LinkedHashSet<String> likes;
    private final String filename;

    private TweetImpl(final String sender, final String sentOn, final String contents, final LinkedHashSet<String> likes, final String filename) {
        this.sender = sender;
        this.sentOn = sentOn;
        this.contents = contents;
        this.likes = likes;
        this.filename = filename;
    }

    @Nullable
    public static Tweet fromFile(final String filename) {
        final Server server = Server.getServer();
        final Database db = server.getTweetDatabase();
        if (db.fileExists(filename)) {
            final JSONObject object = new JSONObject(db.readFile(filename));
            final String sender = object.getString("sender"); // server.getUserStorage().findUser(
            final String sentOn = object.getString("sentOn");
            final String contents = object.getString("contents");
            final LinkedHashSet<String> likes = new LinkedHashSet<>();
            for (Object i : object.getJSONArray("likes")) {
                likes.add((String) i);
            }
            return new TweetImpl(sender, sentOn, contents, likes, filename);
        } else {
            return null;
        }
    }

    private static String randomSuffix() {
        return Integer.toHexString((int) (Math.random() * Integer.MAX_VALUE));
    }

    public static Tweet create(final User user, final String contents) {
        final String sentOn = Server.getServer().getDateFormat().format(Date.from(Instant.now()));
        final String filename = sentOn + "-" + randomSuffix();
        final TweetImpl tweet = new TweetImpl(user.getUsername(), sentOn, contents, new LinkedHashSet<>(), filename);
        Server.getServer().getTweetDatabase().writeFile(filename, tweet.serialize());
        return tweet;
    }

    @Override
    public @NotNull String getSender() {
        return sender;
    }

    @Override
    public @NotNull String getContents() {
        return contents;
    }

    @Override
    public @NotNull String getSentOn() {
        return sentOn;
    }

    @Override
    public @NotNull Collection<String> getLikes() {
        return likes;
    }

    @Override
    public @NotNull String getFilename() {
        return null;
    }

    @Override
    public @NotNull String serialize() {
        final JSONObject object = new JSONObject();
        object.put("sender", sender);
        object.put("sentOn", sentOn);
        object.put("contents", contents);
        object.put("likes", new JSONArray(likes));
        return object.toString();
    }
}