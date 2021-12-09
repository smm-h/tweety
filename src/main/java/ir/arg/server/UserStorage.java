package ir.arg.server;

public interface UserStorage {

    default boolean usernameExists(final String username) {
        return usernameExistsInMemory(username) || usernameExistsOnDisk(username);
    }

    boolean usernameExistsOnDisk(final String username);

    boolean usernameExistsInMemory(final String username);

    default User findUser(final String username) {
        if (!usernameExistsInMemory(username)) {
            addUserToMemory(findUserOnDisk(username));
        }
        return findUserInMemory(username);
    }

    void addUserToMemory(User user);

    User findUserInMemory(final String username);

    User findUserOnDisk(final String username);
}