package ir.arg.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface User extends DatabaseElement {

    @Override
    default @NotNull Database getDatabase() {
        return ServerSingleton.getServer().getUserDatabase();
    }

    @NotNull String getUsername();

    @NotNull String getName();

    @NotNull String getBio();

    @NotNull String getPasswordHash();

    /**
     * This index always refers to either -1 or a valid tweet that
     * has once been sent, even though it may have been deleted later.
     *
     * @return The user's last tweet index
     */
    int getLastTweetIndex();

    int incrementLastTweetIndex();

    @Nullable
    String getTweetAtIndex(int index);

    default String represent() {
        return "@" + getUsername() + "\nName: " + getName() + "\nBio: " + getBio();
    }

    @NotNull
    Set<String> getFollowers();

    default int getFollowersCount() {
        return getFollowers().size();
    }

    @NotNull
    Set<String> getFollowing();

    default int getFollowingCount() {
        return getFollowing().size();
    }

    default boolean isFollowing(@NotNull final String username) {
        return getFollowing().contains(username);
    }

    default boolean isFollowing(@NotNull final User user) {
        return isFollowing(user.getUsername());
    }

    default boolean isFollowedBy(@NotNull final String username) {
        return getFollowers().contains(username);
    }

    default boolean isFollowedBy(@NotNull final User user) {
        return isFollowedBy(user.getUsername());
    }
}
