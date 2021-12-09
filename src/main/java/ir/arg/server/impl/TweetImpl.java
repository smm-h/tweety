package ir.arg.server.impl;

import ir.arg.server.Tweet;
import ir.arg.server.User;

import java.util.Collection;
import java.util.Date;

public class TweetImpl implements Tweet {
    private final User sender;
    private final String contents;
    private final Date sentOn;
    private final Collection<User> likers;

    public TweetImpl(final User sender, final String contents, final Date sentOn, final Collection<User> likers) {
        this.sender = sender;
        this.contents = contents;
        this.sentOn = sentOn;
        this.likers = likers;
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
    public Collection<User> getLikers() {
        return likers;
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
    public String getData() {
        return null;
    }

    @Override
    public void setData(String data) {

    }
}