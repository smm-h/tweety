package ir.arg.server.impl;

import ir.arg.server.Database;
import ir.arg.server.Server;
import ir.arg.server.ServerSingleton;
import ir.arg.server.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class UserImpl implements User {

    private final String username;
    private String name;
    private String bio;
    private String passwordHash;
    private final Set<String> followers, following;
    private final List<String> tweets;

    private UserImpl(@NotNull final String username, @NotNull final String name, @NotNull final String bio, @NotNull final String passwordHash, @NotNull final Set<String> followers, @NotNull final Set<String> following, @NotNull final List<String> tweets) {
        this.username = username;
        this.name = name;
        this.bio = bio;
        this.passwordHash = passwordHash;
        this.followers = followers;
        this.following = following;
        this.tweets = tweets;
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
            final Set<String> followers = JSONHelper.getStringSet(object, "followers");
            final Set<String> following = JSONHelper.getStringSet(object,"following");
            final List<String> tweets = JSONHelper.getStringList(object, "tweets");
            return new UserImpl(filename, name, bio, passwordHash, followers, following, tweets);
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

    @NotNull
    @Override
    public List<String> getTweets() {
        return tweets;
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
        object.put("followers", followers);
        object.put("following", following);
        object.put("tweets", tweets);
        return object;
    }
}