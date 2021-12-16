package ir.arg.server;

import ir.arg.server.contracts.Contract;
import ir.arg.server.impl.PaginatedIteration;
import ir.arg.server.impl.TweetImpl;
import ir.arg.shared.APIMethods;
import ir.arg.shared.ErrorCode;
import ir.arg.shared.RandomHex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;

public interface Methods extends APIMethods, ErrorCode {

    int TODO = 7000;

    static Method find(String method) {
        switch (method) {
            case USERNAME_EXISTS:
                return usernameExists;
            case SEARCH_USERNAME:
                return searchUsername;
            case GET_USER_INFO:
                return getUserInfo;
            case GET_TWEET_INFO:
                return getTweetInfo;
            case GET_TWEET_LIKES:
                return getTweetLikes;
            case GET_TWEETS_OF_USER:
                return getTweetsOfUser;
            case GET_FOLLOWERS_OF_USER:
                return getFollowersOfUser;
            case GET_FOLLOWING_OF_USER:
                return getFollowingOfUser;
            case SIGN_UP:
                return signUp;
            case SIGN_IN:
                return signIn;
            case CHANGE_PASSWORD:
                return changePassword;
            case CHANGE_NAME:
                return changeName;
            case CHANGE_BIO:
                return changeBio;
            case GET_SESSIONS:
                return getSessions;
            case GET_SESSION_INFO:
                return getSessionInfo;
            case TERMINATE_SESSION:
                return terminateSession;
            case GET_TIMELINE:
                return getTimeline;
            case CREATE_TWEET:
                return createTweet;
            case DELETE_TWEET:
                return deleteTweet;
            case LIKE_TWEET:
                return likeTweet;
            case UNLIKE_TWEET:
                return unlikeTweet;
            case FOLLOW_USER:
                return followUser;
            case UNFOLLOW_USER:
                return unfollowUser;
            case REMOVE_FOLLOWER:
                return removeFollower;
            default:
                return null;
        }
    }

    Method usernameExists = (app, object) -> {
        final String key = "username";
        if (!object.has(key))
            return app.missing(key);
        final JSONObject output = app.err(NO_ERROR);
        output.put("exists", app.getUserStorage().usernameExists(object.getString(key)));
        return output;
    };

    Method searchUsername = null;

    Method getUserInfo = (app, object) -> {
        final String key = "username";
        if (!object.has(key))
            return app.missing(key);
        final User user = app.getUserStorage().findUser(object.getString(key));
        if (user == null) {
            return app.err(USER_NOT_FOUND);
        } else {
            final JSONObject output = app.err(NO_ERROR);
            output.put("name", user.getName());
            output.put("bio", user.getBio());
            output.put("tweet_count", user.getTweetCount());
            output.put("followers_count", user.getFollowersCount());
            output.put("following_count", user.getFollowingCount());
            return output;
        }
    };

    Method getTweetInfo = (app, object) -> {
        final String key = "tweet_id";
        if (!object.has(key))
            return app.missing(key);
        final Tweet tweet = app.findTweet(object.getString(key));
        if (tweet == null) {
            return app.err(TWEET_NOT_FOUND);
        } else {
            final JSONObject output = app.err(NO_ERROR);
            output.put("sender", tweet.getSender());
            output.put("sent_on", tweet.getSentOn());
            output.put("contents", tweet.getContents());
            output.put("like_count", tweet.getLikeCount());
            return output;
        }
    };

    PaginationMethod<String> getTweetLikes = (app, object) -> {
        final String key = "tweet_id";
        if (!object.has(key))
            return app.missing(key);
        final Tweet tweet = app.findTweet(object.getString(key));
        if (tweet == null)
            return app.err(TWEET_NOT_FOUND);
        object.put("pagination_id", app.getPaginationService().add(new PaginatedIteration(tweet.getLikes())));
        return null;
    };

    UserPaginationMethod<JSONObject> getTweetsOfUser = Timeline::of;

    UserPaginationMethod<String> getFollowersOfUser = (user) -> new PaginatedIteration(user.getFollowers());

    UserPaginationMethod<String> getFollowingOfUser = (user) -> new PaginatedIteration(user.getFollowing());

    Method signUp = (app, object) -> app.err(app.getAuthenticationService().signUp(object));

    Method signIn = (app, object) -> app.err(app.getAuthenticationService().signIn(object));

    AuthenticatedMethod changePassword = (app, user, object) -> {

        final String key1 = "old_password";
        if (!object.has(key1))
            return app.missing(key1);

        final String key2 = "new_password";
        if (!object.has(key2))
            return app.missing(key2);

        final String oldPassword = object.getString(key1);
        final AuthenticationService auth = app.getAuthenticationService();
        if (auth.hashPassword(oldPassword).equals(user.getPasswordHash())) {
            final String newPassword = object.getString(key2);
            final Contract<String> contract = auth.getPasswordStrengthContract();
            if (contract.verify(newPassword)) {
                final boolean success = user.setPasswordHash(auth.hashPassword(newPassword));
                return app.err(success ? NO_ERROR : SERVER_MISBEHAVIOR);
            } else {
                return app.err(NEW_PASSWORD_TOO_WEAK);
            }
        } else {
            return app.err(INCORRECT_PASSWORD);
        }
    };

    AuthenticatedMethod changeName = (app, user, object) -> {
        final String key = "new_name";
        if (!object.has(key))
            return app.missing(key);

        final String newName = object.getString(key);
        final Contract<String> contract = app.getNameContract();
        if (contract.verify(newName)) {
            final boolean success = user.setName(newName);
            return app.err(success ? NO_ERROR : SERVER_MISBEHAVIOR);
        } else {
            return app.err(contract.getError(newName));
        }
    };

    AuthenticatedMethod changeBio = (app, user, object) -> {
        final String key = "new_bio";
        if (!object.has(key))
            return app.missing(key);

        final String newBio = object.getString(key);
        final Contract<String> contract = app.getBioContract();
        if (contract.verify(newBio)) {
            final boolean success = user.setBio(newBio);
            return app.err(success ? NO_ERROR : SERVER_MISBEHAVIOR);
        } else {
            return app.err(contract.getError(newBio));
        }
    };

    AuthenticatedMethod getSessions = (app, user, object) -> {
        final JSONObject output = app.err(NO_ERROR);
        JSONArray list = app.getAuthenticationService().getSessions(user);
        output.put("count", list.length());
        output.put("session_id_list", list);
        return output;
    };

    AuthenticatedMethod getSessionInfo = null;

    AuthenticatedMethod terminateSession = (app, user, object) -> {
        final String key = "session_id";
        if (!object.has(key))
            return app.missing(key);
        return app.err(app.getAuthenticationService().terminateSession(user, object.getString(key)));
    };

    AuthenticatedMethod getTimeline = (app, user, object) -> {
        if (!object.has("pagination_id") || object.getString("pagination_id").isEmpty()) {
            object.put("pagination_id", app.getPaginationService().add(Timeline.of(user.getFollowingUsers())));
        }
        final String paginationId = object.getString("pagination_id");
        final Pagination<JSONObject> pagination = app.getPaginationService().find(paginationId);
        if (pagination == null) {
            return app.err(PAGINATION_NOT_FOUND);
        } else {
            final JSONArray list = pagination.getNext(getMaxCount(object));
            final JSONObject output = app.err(NO_ERROR);
            output.put("pagination_id", paginationId);
            output.put("count", list.length());
            output.put("list", list);
            return output;
        }
    };

    AuthenticatedMethod createTweet = (app, user, object) -> {
        final Instant instant = Instant.now();
        final String key = "contents";
        if (!object.has(key))
            return app.missing(key);
        final String contents = object.getString(key);
        final Contract<String> contract = app.getTweetContentsContract();
        if (contract.verify(contents)) {
            final String username = user.getUsername();
            final String sentOn = app.getDateFormat().format(Date.from(instant));
            final String tweetId = sentOn + "-" + username + "-" + RandomHex.generate(16);
            final Tweet tweet = new TweetImpl(username, sentOn, contents, new LinkedHashSet<>(), tweetId);
            user.getTweets().add(tweetId);
            user.markAsModified();
            try {
                app.getTweetDatabase().writeFile(tweetId, tweet.serialize().toString());
            } catch (IOException e) {
                return app.err(DATABASE_MISBEHAVIOR, e);
            }
            return app.err(NO_ERROR);
        } else {
            return app.err(contract.getError(contents));
        }
    };

    AuthenticatedMethod deleteTweet = (app, user, object) -> {
        final String key = "tweet_id";
        if (!object.has(key))
            return app.missing(key);
        final String tweetId = object.getString(key);
        final Database db = app.getTweetDatabase();
        if (db.fileExists(tweetId)) {
            if (user.getTweets().contains(tweetId)) {
                user.getTweets().remove(tweetId);
                user.markAsModified();
                try {
                    db.deleteFile(tweetId);
                    return app.err(NO_ERROR);
                } catch (IOException e) {
                    return app.err(TWEET_NOT_FOUND);
                }
            } else {
                return app.err(TWEET_NOT_OWNED_BY_USER);
            }
        } else {
            return app.err(TWEET_NOT_FOUND);
        }
    };

    AuthenticatedMethod likeTweet = (app, user, object) -> {
        return app.err(TODO);
    };

    AuthenticatedMethod unlikeTweet = (app, user, object) -> {
        return app.err(TODO);
    };

    AuthenticatedMethod followUser = (app, user, object) -> {
        return app.err(TODO);
    };

    AuthenticatedMethod unfollowUser = (app, user, object) -> {
        return app.err(TODO);
    };

    AuthenticatedMethod removeFollower = (app, user, object) -> {
        return app.err(TODO);
    };

    interface Method {

        default @NotNull JSONObject process(@NotNull final App app, @NotNull final JSONObject object) {
            final JSONObject e = preProcess(app, object);
            if (e != null)
                return e;
            return midProcess(app, object);
        }

        default @Nullable JSONObject preProcess(@NotNull final App app, @NotNull final JSONObject object) {
            return null;
        }

        @NotNull JSONObject midProcess(@NotNull final App app, @NotNull final JSONObject object);
    }

    interface AuthenticatedMethod extends Method {
        @Override
        default @NotNull JSONObject midProcess(final @NotNull App app, final @NotNull JSONObject object) {
            final User me;
            try {
                final String myUsername = object.getString("my_username");
                final String token = object.getString("token");
                if (app.getAuthenticationService().authenticate(myUsername, token)) {
                    me = app.getUserStorage().findUser(myUsername);
                    if (me == null) {
                        return app.err(USER_NOT_FOUND);
                    } else {
                        return processWithAuth(app, me, object);
                    }
                } else {
                    return app.err(AUTHENTICATION_FAILED);
                }
            } catch (JSONException e) {
                return app.err(UNAUTHORIZED_REQUEST, e);
            }
        }

        @NotNull
        JSONObject processWithAuth(@NotNull final App app, @NotNull final User user, @NotNull final JSONObject object);
    }

    int MAX_MAX_COUNT = 1000;
    int DEFAULT_MAX_COUNT = 30;

    static int getMaxCount(JSONObject object) {
        if (object.has("max_count")) {
            final int mc = object.getInt("max_count");
            if (mc >= MAX_MAX_COUNT) {
                return MAX_MAX_COUNT;
            } else if (mc <= 0) {
                return DEFAULT_MAX_COUNT;
            } else {
                return mc;
            }
        } else {
            return DEFAULT_MAX_COUNT;
        }
    }

    interface PaginationMethod<T> extends Method {

        @Override
        default @NotNull JSONObject midProcess(final @NotNull App app, final @NotNull JSONObject object) {
            if (!object.has("pagination_id") || object.getString("pagination_id").isEmpty()) {
                final JSONObject e = firstCall(app, object);
                if (e != null)
                    return e;
            }
            final String paginationId = object.getString("pagination_id");
            final Pagination<T> pagination = app.getPaginationService().find(paginationId);
            if (pagination == null) {
                return app.err(PAGINATION_NOT_FOUND);
            } else {
                final JSONArray list = pagination.getNext(getMaxCount(object));
                final JSONObject output = app.err(NO_ERROR);
                output.put("pagination_id", paginationId);
                output.put("count", list.length());
                output.put("list", list);
                return output;
            }
        }

        /**
         * This method is supposed to create a new pagination, add it to the pagination
         * service pool, get its id, and put that id in the JSONObject it receives.
         *
         * @param app    App instance for convenience
         * @param object JSONObject to put the ID in and get other values
         * @return null in case of no errors
         */
        @Nullable
        JSONObject firstCall(@NotNull final App app, @NotNull final JSONObject object);
    }

    interface UserPaginationMethod<T> extends PaginationMethod<T> {
        @Override
        default @Nullable JSONObject firstCall(final @NotNull App app, final @NotNull JSONObject object) {
            final String key = "username";
            if (!object.has(key))
                return app.missing(key);
            final User user = app.findUser(object.getString(key));
            if (user == null)
                return app.err(USER_NOT_FOUND);
            object.put("pagination_id", app.getPaginationService().add(makePagination(user)));
            return null;
        }

        @NotNull
        Pagination<T> makePagination(@NotNull final User user);
    }
}
