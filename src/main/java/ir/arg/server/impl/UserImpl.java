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

    private UserImpl(final String username, final String name, final String bio, final String passwordHash) {
        this.username = username;
        this.name = name;
        this.bio = bio;
        this.passwordHash = passwordHash;
    }

    @Nullable
    public static User fromFile(final String filename) {
        final Server server = ServerSingleton.getServer();
        final Database db = server.getUserDatabase();
        if (db.fileExists(filename)) {
            final JSONObject object = new JSONObject(db.readFile(filename));
            final String name = object.getString("name");
            final String bio = object.getString("bio");
            final String passwordHash = object.getString("passwordHash");
            return new UserImpl(filename, name, bio, passwordHash);
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
        return object.toString();
    }
}