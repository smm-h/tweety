package ir.arg.shared;

public interface ErrorCode {

    int NO_ERROR = 0;
    int USERNAME_EMPTY = 101;
    int PASSWORD_EMPTY = 102;
    int USERNAME_DOES_NOT_EXIST = 103;
    int USERNAME_ALREADY_EXISTS = 104;
    int BAD_USERNAME = 105;
    int PASSWORD_TOO_WEAK = 106;
    int NEW_PASSWORD_TOO_WEAK = 400;
    int INCORRECT_PASSWORD = 107;
    int BAD_TOKEN = 108;
    int TWEET_NOT_OWNED_BY_USER = 201;
    int UNDEFINED_METHOD = 800;
    int FAILED_TO_PARSE_REQUEST = 801;
    int PARAMS_MISSING = 802;
    int UNAUTHORIZED_REQUEST = 803;
    int METHOD_MISSING = 804;
    int CONTRACT_VOIDED = 805;
    int AUTHENTICATION_FAILED = 900;
    int USER_NOT_FOUND = 901;
    int TWEET_NOT_FOUND = 902;
    int SESSION_NOT_FOUND = 903;
    int PAGINATION_NOT_FOUND = 904;
    int SERVER_MISBEHAVIOR = 990;
    int DATABASE_MISBEHAVIOR = 991;
    int UNCAUGHT = 999;

    static String getErrorDescription(final int errorCode) {
        switch (errorCode) {
            case NO_ERROR:
                return "Successful";
            case USERNAME_EMPTY:
                return "The username cannot be empty";
            case PASSWORD_EMPTY:
                return "The password cannot be empty";
            case USERNAME_DOES_NOT_EXIST:
                return "The entered username does not match any accounts";
            case USERNAME_ALREADY_EXISTS:
                return "The entered username already exists";
            case BAD_USERNAME:
                return "The entered username is not a valid username";
            case PASSWORD_TOO_WEAK:
                return "The password is not secure enough";
            case NEW_PASSWORD_TOO_WEAK:
                return "The new password is not secure enough";
            case INCORRECT_PASSWORD:
                return "The password you entered was incorrect";
            case BAD_TOKEN:
                return "The token is invalid";
            case UNDEFINED_METHOD:
                return "This method is undefined";
            case FAILED_TO_PARSE_REQUEST:
                return "Failed to parse the request JSON";
            case PARAMS_MISSING:
                return "The request is missing parameters";
            case UNAUTHORIZED_REQUEST:
                return "This method requires valid authentication";
            case METHOD_MISSING:
                return "The method parameter is missing from the request";
            case AUTHENTICATION_FAILED:
                return "Access denied";
            case USER_NOT_FOUND:
                return "User not found";
            case TWEET_NOT_FOUND:
                return "Tweet not found; it may have been deleted";
            case SESSION_NOT_FOUND:
                return "Session not found; it may been terminated or expired";
            case PAGINATION_NOT_FOUND:
                return "Pagination not found; it may have expired";
            case SERVER_MISBEHAVIOR:
                return "The server misbehaved; try again please";
            case DATABASE_MISBEHAVIOR:
                return "The database misbehaved; try again please";
            case CONTRACT_VOIDED:
                return "Contract was voided";
            case UNCAUGHT:
                return "Internal error";
            default:
                return "No description is available for this error code: " + errorCode;
        }
    }
}