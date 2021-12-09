package ir.arg.server.impl;

import ir.arg.server.Server;
import ir.arg.server.User;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class UserImpl implements User {

    private final String username;
    private final String name;
    private final String bio;
    private final String passwordHash;

    private boolean isOnDisk;

    private UserImpl(final String username, final String name, final String bio, final String passwordHash) {
        this.username = username;
        this.name = name;
        this.bio = bio;
        this.passwordHash = passwordHash;
        this.isOnDisk = false;
    }

    public static UserImpl findOnDisk(final String username) {
        final String address = Server.getServer().getProperties().getUserDatabase() + username;
        return null;
    }

    public String represent() {
        return "@" + username + "\nName: " + name + "\nBio: " + bio;
    }

    @Override
    public String toString() {
        return "User@" + username;
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
    public String getFilename() {
        return username;
    }

    @Override
    public boolean isOnDisk() {
        return isOnDisk;
    }

    @Override
    public @NotNull String serialize() {
        return null;
    }
}