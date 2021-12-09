package ir.arg.server.impl;

import ir.arg.server.Tweet;
import ir.arg.server.User;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class TweetImpl implements Tweet {

    private final User sender;
    private final String contents;
    private final Date sentOn;
    private final Collection<User> likes;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public TweetImpl(final User sender, final String contents, final Date sentOn, final Collection<User> likes) {
        this.sender = sender;
        this.contents = contents;
        this.sentOn = sentOn;
        this.likes = likes;
    }

    @Override
    public User getSender() {
        return sender;
    }

    @Override
    public String getContents() {
        return contents;
    }

    @Override
    public Date getSentOn() {
        return sentOn;
    }

    @Override
    public Collection<User> getLikes() {
        return likes;
    }

    @Override
    public String getFilename() {
        return null;
    }

    @Override
    public boolean isOnDisk() {
        return false;
    }

    @Override
    public @NotNull String serialize() {
        final JSONObject object = new JSONObject();
        object.put("sender", sender.getUsername());
        object.put("contents", contents);
        object.put("sentOn", DATE_FORMAT.format(sentOn));
        object.put("likes", new JSONArray(likes));
        return object.toString();
    }
}