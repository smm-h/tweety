package ir.arg.server.impl;

import ir.arg.server.Server;
import ir.arg.server.User;

import java.util.HashMap;
import java.util.Map;

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
    public String getUsername() {
        return username;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getBio() {
        return bio;
    }

    @Override
    public String getPasswordHash() {
        return passwordHash;
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