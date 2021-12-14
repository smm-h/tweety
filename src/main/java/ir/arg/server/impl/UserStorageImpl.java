package ir.arg.server.impl;

import ir.arg.server.App;
import ir.arg.server.User;
import ir.arg.server.UserStorage;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserStorageImpl implements UserStorage {
    private final Map<String, User> usersInMemory = new HashMap<>();


    @Override
    public boolean usernameExistsInMemory(String username) {
        return usersInMemory.containsKey(username);
    }

    @Override
    public @Nullable User findUserInMemory(String username) {
        return usersInMemory.get(username);
    }

    @Override
    public boolean usernameExistsOnDisk(String username) {
        return App.getInstance().getUserDatabase().fileExists(username);
    }

    @Nullable
    @Override
    public User findUserOnDisk(String username) {
        try {
            return UserImpl.fromFile(username);
        } catch (IOException e) {
            App.getInstance().getUserDatabase().log(e);
            return null;
        }
    }

    @Override
    public void addUserToMemory(User user) {
        usersInMemory.put(user.getUsername(), user);
    }
}
