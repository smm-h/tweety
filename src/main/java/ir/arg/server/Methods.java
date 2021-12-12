package ir.arg.server;

import ir.arg.server.contracts.Contract;
import ir.arg.server.impl.TweetImpl;
import ir.arg.server.shared.APIMethods;
import ir.arg.server.shared.ErrorCode;
import ir.arg.server.shared.RandomHex;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;

public interface Methods extends APIMethods, ErrorCode {

    int TODO = 7000;
    Method undefined = null;

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
                return undefined;
        }
    }

    Method usernameExists = (server, object) -> {
        final String key = "username";
        if (object.has(key)) {
            final JSONObject output = server.err(NO_ERROR);
            output.put("exists", server.getUserStorage().usernameExists(object.getString(key)));
            return output;
        } else {
            return server.missing(key);
        }
    };

    Method searchUsername = undefined;

    Method getUserInfo = (server, object) -> {
        final String key = "username";
        if (object.has(key)) {
            final User user = server.getUserStorage().findUser(object.getString(key));
            if (user == null) {
                return server.err(USER_NOT_FOUND);
            } else {
                final JSONObject output = server.err(NO_ERROR);
                output.put("name", user.getName());
                output.put("bio", user.getBio());
                output.put("followers_count", user.getFollowersCount());
                output.put("following_count", user.getFollowingCount());
                return output;
            }
        } else {
            return server.missing(key);
        }
    };

    Method getTweetInfo = (server, object) -> {
        final String key = "tweet_id";
        if (object.has(key)) {
            final Tweet tweet = server.findTweet(object.getString(key));
            if (tweet == null) {
                return server.err(TWEET_NOT_FOUND);
            } else {
                final JSONObject output = server.err(NO_ERROR);
                output.put("sender", tweet.getSender());
                output.put("sent_on", tweet.getSentOn());
                output.put("contents", tweet.getContents());
                output.put("like_count", tweet.getLikeCount());
                return output;
            }
        } else {
            return server.missing(key);
        }
    };

    Method getTweetLikes = (server, object) -> {
        return server.err(TODO);
    };

    Method getTweetsOfUser = (server, object) -> {
        return server.err(TODO);
    };

    Method getFollowersOfUser = (server, object) -> {
        return server.err(TODO);
    };

    Method getFollowingOfUser = (server, object) -> {
        return server.err(TODO);
    };

    Method signUp = (server, object) -> server.err(server.getAuthenticationService().signUp(object));

    Method signIn = (server, object) -> server.err(server.getAuthenticationService().signIn(object));

    MethodWithAuth changePassword = (server, user, object) -> {
        final String key1 = "old_password";
        final String key2 = "new_password";
        if (object.has(key1)) {
            if (object.has(key2)) {
                final String oldPassword = object.getString(key1);
                final AuthenticationService auth = server.getAuthenticationService();
                if (auth.hashPassword(oldPassword).equals(user.getPasswordHash())) {
                    final String newPassword = object.getString(key2);
                    final Contract<String> contract = auth.getPasswordStrengthContract();
                    if (contract.verify(newPassword)) {
                        final boolean success = user.setPasswordHash(auth.hashPassword(newPassword));
                        return server.err(success ? NO_ERROR : SERVER_MISBEHAVIOR);
                    } else {
                        return server.err(NEW_PASSWORD_TOO_WEAK);
                    }
                } else {
                    return server.err(INCORRECT_PASSWORD);
                }
            } else return server.missing(key2);
        } else return server.missing(key1);
    };

    MethodWithAuth changeName = (server, user, object) -> {
        final String key = "new_name";
        if (object.has(key)) {
            final String newName = object.getString(key);
            final Contract<String> contract = server.getNameContract();
            if (contract.verify(newName)) {
                final boolean success = user.setName(newName);
                return server.err(success ? NO_ERROR : SERVER_MISBEHAVIOR);
            } else {
                return server.err(contract.getError(newName));
            }
        } else {
            return server.missing(key);
        }
    };

    MethodWithAuth changeBio = (server, user, object) -> {
        final String key = "new_bio";
        if (object.has(key)) {
            final String newBio = object.getString(key);
            final Contract<String> contract = server.getBioContract();
            if (contract.verify(newBio)) {
                final boolean success = user.setBio(newBio);
                return server.err(success ? NO_ERROR : SERVER_MISBEHAVIOR);
            } else {
                return server.err(contract.getError(newBio));
            }
        } else {
            return server.missing(key);
        }
    };

    MethodWithAuth getSessions = (server, user, object) -> {
        final JSONObject output = server.err(NO_ERROR);
        JSONArray list = server.getAuthenticationService().getSessions(user);
        output.put("count", list.length());
        output.put("session_id_list", list);
        return output;
    };

    MethodWithAuth getSessionInfo = (server, user, object) -> {
        return server.err(TODO);
    };

    MethodWithAuth terminateSession = (server, user, object) -> {
        final String key = "session_id";
        if (object.has(key)) {
            return server.err(server.getAuthenticationService().terminateSession(user, object.getString(key)));
        } else {
            return server.missing(key);
        }
    };

    MethodWithAuth getTimeline = (server, user, object) -> {
        return server.err(TODO);
    };

    MethodWithAuth createTweet = (server, user, object) -> {
        final Instant instant = Instant.now();
        final String key = "contents";
        if (object.has(key)) {
            final String contents = object.getString(key);
            final Contract<String> contract = server.getTweetContentsContract();
            if (contract.verify(contents)) {
                final String username = user.getUsername();
                final int index = user.incrementLastTweetIndex();
                final String sentOn = server.getDateFormat().format(Date.from(instant));
                final String filename = sentOn + "-" + username + "-" + index + "-" + RandomHex.generate(16);
                final Tweet tweet = new TweetImpl(username, index, sentOn, contents, new LinkedHashSet<>(), filename);
                server.getTweetDatabase().writeFile(filename, tweet.serialize().toString());
                return server.err(NO_ERROR);
            } else {
                return server.err(contract.getError(contents));
            }
        } else {
            return server.missing(key);
        }
    };

    MethodWithAuth deleteTweet = (server, user, object) -> {
        final String key = "tweet_id";
        if (object.has(key)) {
            final String tweetId = object.getString(key);
            final Database db = server.getTweetDatabase();
            if (db.fileExists(tweetId)) {
                return server.err(db.deleteFile(tweetId) ? NO_ERROR : TWEET_NOT_FOUND);
            } else {
                return server.err(TWEET_NOT_FOUND);
            }
        } else {
            return server.missing(key);
        }
    };

    MethodWithAuth likeTweet = (server, user, object) -> {
        return server.err(TODO);
    };

    MethodWithAuth unlikeTweet = (server, user, object) -> {
        return server.err(TODO);
    };

    MethodWithAuth followUser = (server, user, object) -> {
        return server.err(TODO);
    };

    MethodWithAuth unfollowUser = (server, user, object) -> {
        return server.err(TODO);
    };

    MethodWithAuth removeFollower = (server, user, object) -> {
        return server.err(TODO);
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
