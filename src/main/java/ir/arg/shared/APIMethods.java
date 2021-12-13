package ir.arg.shared;

public interface APIMethods {
    String USERNAME_EXISTS = "username_exists";
    String SEARCH_USERNAME = "search_username";
    String GET_USER_INFO = "get_user_info";
    String GET_TWEET_INFO = "get_tweet_info";
    String GET_TWEET_LIKES = "get_tweet_likes";
    String GET_TWEETS_OF_USER = "get_tweets_of_user";
    String GET_FOLLOWERS_OF_USER = "get_followers_of_user";
    String GET_FOLLOWING_OF_USER = "get_following_of_user";
    String SIGN_UP = "sign_up";
    String SIGN_IN = "sign_in";
    String CHANGE_PASSWORD = "change_password";
    String CHANGE_NAME = "change_name";
    String CHANGE_BIO = "change_bio";
    String GET_SESSIONS = "get_sessions";
    String GET_SESSION_INFO = "get_session_info";
    String TERMINATE_SESSION = "terminate_session";
    String GET_TIMELINE = "get_timeline";
    String CREATE_TWEET = "create_tweet";
    String DELETE_TWEET = "delete_tweet";
    String LIKE_TWEET = "like_tweet";
    String UNLIKE_TWEET = "unlike_tweet";
    String FOLLOW_USER = "follow_user";
    String UNFOLLOW_USER = "unfollow_user";
    String REMOVE_FOLLOWER = "remove_follower";
}
