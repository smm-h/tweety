package ir.arg.server.impl;

import ir.arg.server.App;
import ir.arg.server.Database;
import ir.arg.server.Tweet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Set;

public class TweetImpl implements Tweet {

    private final String sender;
    private final String sentOn;
    private final String contents;
    private final Set<String> likes;
    private final String filename;

    public TweetImpl(final String sender, final String sentOn, final String contents, final Set<String> likes, final String filename) {
        this.sender = sender;
        this.sentOn = sentOn;
        this.contents = contents;
        this.likes = likes;
        this.filename = filename;
    }

    @Nullable
    public static Tweet fromFile(final String filename) throws IOException {
        final App app = App.getInstance();
        final Database db = app.getTweetDatabase();
        if (db.fileExists(filename)) {
            final String fileContents = db.readFile(filename);
            assert fileContents != null;
            final JSONObject object = new JSONObject(fileContents);
            final String sender = object.getString("sender");
            final String sentOn = object.getString("sentOn");
            final String contents = object.getString("contents");
            final Set<String> likes = JSONHelper.getStringSet(object, "likes");
            return new TweetImpl(sender, sentOn, contents, likes, filename);
        } else {
            return null;
        }
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
    public @NotNull Set<String> getLikes() {
        return likes;
    }

    @Override
    public @NotNull String getFilename() {
        return filename;
    }

    @Override
    public @NotNull JSONObject serialize() {
        final JSONObject object = new JSONObject();
        object.put("sender", sender);
        object.put("sentOn", sentOn);
        object.put("contents", contents);
        object.put("likes", new JSONArray(likes));
        return object;
    }
}