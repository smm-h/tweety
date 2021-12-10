package ir.arg.server;

import org.jetbrains.annotations.Nullable;

public interface UserStorage {

    default boolean usernameExists(final String username) {
        return usernameExistsInMemory(username) || usernameExistsOnDisk(username);
    }

    boolean usernameExistsOnDisk(final String username);

    boolean usernameExistsInMemory(final String username);

    @Nullable
    default User findUser(final String username) {
        if (!usernameExistsInMemory(username)) {
            addUserToMemory(findUserOnDisk(username));
        }
        return findUserInMemory(username);
    }

    void addUserToMemory(User user);

    @Nullable
    User findUserInMemory(final String username);

    @Nullable
    User findUserOnDisk(final String username);
}