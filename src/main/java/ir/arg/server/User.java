package ir.arg.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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

    boolean setName(@NotNull String name);

    boolean setBio(@NotNull String bio);

    boolean setPasswordHash(@NotNull String passwordHash);

    @NotNull
    List<String> getTweets();

    default int getTweetCount() {
        return getTweets().size();
    }

    @Nullable
    default String getTweetAtIndex(final int index) {
        return getTweets().get(index);
    }

    default void setTweetAtIndexToNull(final int index) {
        getTweets().set(index, null);
    }

    @NotNull
    Set<String> getFollowers();

    default int getFollowersCount() {
        return getFollowers().size();
    }

    @NotNull
    Set<String> getFollowing();
    @NotNull
    Set<User> getFollowingUsers();

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
