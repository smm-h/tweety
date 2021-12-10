package ir.arg.server.impl;

import ir.arg.server.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class TweetImpl implements Tweet {

    private final String sender;
    private final int index;
    private final String sentOn;
    private final String contents;
    private final LinkedHashSet<String> likes;
    private final String filename;

    TweetImpl(final String sender, final int index, final String sentOn, final String contents, final LinkedHashSet<String> likes, final String filename) {
        this.sender = sender;
        this.index = index;
        this.sentOn = sentOn;
        this.contents = contents;
        this.likes = likes;
        this.filename = filename;
    }

    @Nullable
    public static Tweet fromFile(final String filename) {
        final Server server = ServerSingleton.getServer();
        final Database db = server.getTweetDatabase();
        if (db.fileExists(filename)) {
            final String fileContents = db.readFile(filename);
            assert fileContents != null;
            final JSONObject object = new JSONObject(fileContents);
            final String sender = object.getString("sender");
            final int index = object.getInt("index");
            final String sentOn = object.getString("sentOn");
            final String contents = object.getString("contents");
            final LinkedHashSet<String> likes = new LinkedHashSet<>();
            for (Object i : object.getJSONArray("likes")) {
                likes.add((String) i);
            }
            return new TweetImpl(sender, index, sentOn, contents, likes, filename);
        } else {
            return null;
        }
    }

    @Override
    public @NotNull String getSender() {
        return sender;
    }

    @Override
    public int getIndex() {
        return index;
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
    public @NotNull Set<String> getLikes() {
        return likes;
    }

    public @NotNull String getFilename() {
        return filename;
    }

    @Override
    public @NotNull JSONObject serialize() {
        final JSONObject object = new JSONObject();
        object.put("sender", sender);
        object.put("index", index);
        object.put("sentOn", sentOn);
        object.put("contents", contents);
        object.put("likes", new JSONArray(likes));
        return object;
    }
}