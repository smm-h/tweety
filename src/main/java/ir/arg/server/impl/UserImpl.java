package ir.arg.server.impl;

import ir.arg.server.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class UserImpl implements User {

    private final String username;
    private final String name;
    private final String bio;
    private final String passwordHash;
    private int lastTweetIndex;

    private UserImpl(final String username, final String name, final String bio, final String passwordHash, final int lastTweetIndex) {
        this.username = username;
        this.name = name;
        this.bio = bio;
        this.passwordHash = passwordHash;
        this.lastTweetIndex = lastTweetIndex;
    }

    @Nullable
    public static User fromFile(final String filename) {
        final Server server = ServerSingleton.getServer();
        final Database db = server.getUserDatabase();
        if (db.fileExists(filename)) {
            final String fileContents = db.readFile(filename);
            assert fileContents != null;
            final JSONObject object = new JSONObject(fileContents);
            final String name = object.getString("name");
            final String bio = object.getString("bio");
            final String passwordHash = object.getString("passwordHash");
            final int lastTweetIndex = object.getInt("lastTweetIndex");
            return new UserImpl(filename, name, bio, passwordHash, lastTweetIndex);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "@" + username;
    }

    @Override
    public @NotNull String getUsername() {
        return username;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getBio() {
        return bio;
    }

    @Override
    public @NotNull String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public int getLastTweetIndex() {
        return lastTweetIndex;
    }

    @Override
    public int incrementLastTweetIndex() {
        lastTweetIndex++;
        // TODO write me to file
        return lastTweetIndex;
    }

    @Override
    public @Nullable String getTweetAtIndex(int index) {
        final String filename = username + "-" + index;
        final Database db = ServerSingleton.getServer().getUserTweetsDatabase();
        return db.fileExists(filename) ? db.readFile(filename) : null;
    }

    @Override
    public @NotNull String getFilename() {
        return username;
    }

    @Override
    public @NotNull String serialize() {
        final JSONObject object = new JSONObject();
        // object.put("username", username);
        object.put("name", name);
        object.put("bio", bio);
        object.put("passwordHash", passwordHash);
        object.put("lastTweetIndex", lastTweetIndex);
        return object.toString();
    }
}