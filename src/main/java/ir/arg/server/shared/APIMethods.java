package ir.arg.server.shared;

public interface APIMethods {

    String USERNAME_EXISTS = "username_exists";
    String SEARCH_USER = "search_user";
    String GET_USER_INFO = "get_user_info";

    String GET_TWEETS = "get_tweets";
    String GET_FOLLOWERS = "get_followers";
    String GET_FOLLOWING = "get_following";

    String SIGN_UP = "sign_up";
    String SIGN_IN = "sign_in";

    String CHANGE_PASSWORD = "change_password";
    String CHANGE_NAME = "change_name";
    String CHANGE_BIO = "change_bio";

    String GET_SESSIONS = "get_sessions";
    String GET_TIMELINE = "get_timeline";

    String CREATE_TWEET = "create_tweet";
    String DELETE_TWEET = "delete_tweet";

    String LIKE_TWEET = "like_tweet";
    String UNLIKE_TWEET = "unlike_tweet";

    String FOLLOW_USER = "follow_user";
    String UNFOLLOW_USER = "unfollow_user";
    String REMOVE_FOLLOWER = "remove_follower";
}
