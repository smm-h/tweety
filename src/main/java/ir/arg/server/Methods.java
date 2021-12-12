package ir.arg.server;

import ir.arg.server.impl.TweetImpl;
import ir.arg.server.shared.APIMethods;
import ir.arg.server.shared.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;

public interface Methods extends APIMethods, ErrorCode {

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

    Method usernameExists = (server, object) -> {
        final String key = "username";
        if (object.has(key)) {
            final JSONObject output = server.err(NO_ERROR);
            output.put("exists", server.getUserStorage().usernameExists(object.getString(key)));
            return output;
        } else {
            return server.err(METHOD_MISSING);
        }
    };
    Method searchUsername = (server, object) -> {
        // TODO
        return null;
    };
    Method getUserInfo = (server, object) -> {
        // TODO
        return null;
    };
    Method getTweetInfo = (server, object) -> {
        // TODO
        return null;
    };
    Method getTweetLikes = (server, object) -> {
        // TODO
        return null;
    };
    Method getTweetsOfUser = (server, object) -> {
        // TODO
        return null;
    };
    Method getFollowersOfUser = (server, object) -> {
        // TODO
        return null;
    };
    Method getFollowingOfUser = (server, object) -> {
        // TODO
        return null;
    };

    Method signUp = (server, object) -> server.err(server.getAuthenticationService().signUp(object));

    Method signIn = (server, object) -> server.err(server.getAuthenticationService().signIn(object));

    MethodWithAuth changePassword = (server, user, object) -> {
        // TODO
        return null;
    };

    MethodWithAuth changeName = (server, user, object) -> {
        // TODO
        return null;
    };

    MethodWithAuth changeBio = (server, user, object) -> {
        // TODO
        return null;
    };

    MethodWithAuth getSessions = (server, user, object) -> {
        // TODO
        return null;
    };

    MethodWithAuth getSessionInfo = (server, user, object) -> {
        // TODO
        return null;
    };

    MethodWithAuth terminateSession = (server, user, object) -> {
        // TODO
        return null;
    };

    MethodWithAuth getTimeline = (server, user, object) -> {
        // TODO
        return null;
    };

    static String randomSuffix() {
        return Integer.toHexString((int) (Math.random() * Integer.MAX_VALUE));
    }

    MethodWithAuth createTweet = (server, user, object) -> {
        final String sentOn = server.getDateFormat().format(Date.from(Instant.now()));
        final String username = user.getUsername();
        final int index = user.incrementLastTweetIndex();
        final String filename = sentOn + "-" + username + "-" + index + "-" + randomSuffix();
        final TweetImpl tweet = new TweetImpl(username, index, sentOn, contents, new LinkedHashSet<>(), filename);
        server.getTweetDatabase().writeFile(filename, tweet.serialize().toString());
    };

    MethodWithAuth deleteTweet = (server, user, object) -> {
        final String filename;
        final String key = "tweet_id";
        if (object.has(key)) {
            filename = object.getString(key);
            final Database db = server.getTweetDatabase();
            return db.fileExists(filename) && db.deleteFile(filename);
        } else {
            return server.err(PARAMS_MISSING);
        }
    };

    MethodWithAuth likeTweet = (server, user, object) -> {
        // TODO
        return null;
    };

    MethodWithAuth unlikeTweet = (server, user, object) -> {
        // TODO
        return null;
    };

    MethodWithAuth followUser = (server, user, object) -> {
        // TODO
        return null;
    };

    MethodWithAuth unfollowUser = (server, user, object) -> {
        // TODO
        return null;
    };

    MethodWithAuth removeFollower = (server, user, object) -> {
        // TODO
        return null;
    };


    interface Method {
        @NotNull
        JSONObject process(@NotNull final Server server, @NotNull final JSONObject object);
    }

    interface MethodWithAuth extends Method {

        @Override
        default @NotNull JSONObject process(final @NotNull Server server, final @NotNull JSONObject object) {
            final User me;
            try {
                final String myUsername = object.getString("my_username");
                final String token = object.getString("token");
                if (server.getAuthenticationService().authenticate(myUsername, token)) {
                    me = server.getUserStorage().findUser(myUsername);
                    if (me == null) {
                        return server.err(USER_NOT_FOUND);
                    } else {
                        return processWithAuth(server, me, object);
                    }
                } else {
                    return server.err(AUTHENTICATION_FAILED);
                }
            } catch (JSONException e) {
                return server.err(UNAUTHORIZED_REQUEST, e);
            }
        }

        @NotNull
        JSONObject processWithAuth(@NotNull final Server server, @NotNull final User user, @NotNull final JSONObject object);
    }
}
