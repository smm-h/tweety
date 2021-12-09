package ir.arg.server.impl;

import ir.arg.server.User;
import ir.arg.server.UserStorage;

import java.util.HashMap;
import java.util.Map;

public class UserStorageImpl implements UserStorage {
    private final Map<String, User> usersInMemory = new HashMap<>();

    @Override
    public boolean usernameExistsOnDisk(String username) {
        return false;
    }

    @Override
    public boolean usernameExistsInMemory(String username) {
        return usersInMemory.containsKey(username);
    }

    @Override
    public User findUserInMemory(String username) {
        return usersInMemory.get(username);
    }

    @Override
    public User findUserOnDisk(String username) {
        return UserImpl.findOnDisk(username);
    }

    @Override
    public void addUserToMemory(User user) {
        usersInMemory.put(user.getUsername(), user);
    }
}
