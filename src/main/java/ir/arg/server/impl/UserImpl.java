package ir.arg.server.impl;

import ir.arg.server.Database;
import ir.arg.server.Server;
import ir.arg.server.ServerSingleton;
import ir.arg.server.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class UserImpl implements User {

    private final String username;
    private String name;
    private String bio;
    private String passwordHash;
    private int lastTweetIndex;
    private final Set<String> followers, following;

    private UserImpl(final String username, final String name, final String bio, final String passwordHash, final int lastTweetIndex, Set<String> followers, Set<String> following) {
        this.username = username;
        this.name = name;
        this.bio = bio;
        this.passwordHash = passwordHash;
        this.lastTweetIndex = lastTweetIndex;
        this.followers = followers;
        this.following = following;
    }

    @Nullable
    public static User fromFile(final String filename) throws IOException {
        final Server server = ServerSingleton.getServer();
        final Database db = server.getUserDatabase();
        if (db.fileExists(filename)) {
            final String fileContents = db.readFile(filename);
            assert fileContents != null;
            final JSONObject object = new JSONObject(fileContents);
            final String passwordHash = object.getString("passwordHash");
            final String name = object.has("name") ? object.getString("name") : "";
            final String bio = object.has("bio") ? object.getString("bio") : "";
            final int lastTweetIndex = object.has("lastTweetIndex") ? object.getInt("lastTweetIndex") : -1;
            final Set<String> followers = object.has("followers") ? JSONHelper.getStringSet(object.getJSONArray("followers")) : new HashSet<>();
            final Set<String> following = object.has("following") ? JSONHelper.getStringSet(object.getJSONArray("following")) : new HashSet<>();
            return new UserImpl(filename, name, bio, passwordHash, lastTweetIndex, followers, following);
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
    public boolean setName(@NotNull String name) {
        if (name.length() <= 64) {
            this.name = name;
            markAsModified();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setBio(@NotNull String bio) {
        if (name.length() <= 64) {
            this.bio = bio;
            markAsModified();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setPasswordHash(@NotNull String passwordHash) {
        if (name.length() <= 64) {
            this.passwordHash = passwordHash;
            markAsModified();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getLastTweetIndex() {
        return lastTweetIndex;
    }

    @Override
    public int incrementLastTweetIndex() {
        lastTweetIndex++;
        markAsModified();
        return lastTweetIndex;
    }

    @Override
    public @Nullable String getTweetAtIndex(int index) {
        final String filename = username + "-" + index;
        final Database db = ServerSingleton.getServer().getUserTweetsDatabase();
        return db.fileExists(filename) ? db.readFile(filename) : null;
    }

    @Override
    public @NotNull Set<String> getFollowers() {
        return followers;
    }

    @Override
    public @NotNull Set<String> getFollowing() {
        return following;
    }

    @Override
    public @NotNull String getFilename() {
        return username;
    }

    @Override
    public @NotNull JSONObject serialize() {
        final JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("bio", bio);
        object.put("passwordHash", passwordHash);
        object.put("lastTweetIndex", lastTweetIndex);
        object.put("followers", followers);
        object.put("following", following);
        return object;
    }
}