package ir.arg.server;

public interface ErrorCode {

    int NO_ERROR = 0;

    int USERNAME_EMPTY = 101;
    int PASSWORD_EMPTY = 102;
    int USERNAME_DOES_NOT_EXIST = 103;
    int USERNAME_ALREADY_EXISTS = 104;
    int BAD_USERNAME = 105;
    int PASSWORD_TOO_WEAK = 106;
    int INCORRECT_PASSWORD = 107;
    int INVALID_CLIENT = 108;

    int UNDEFINED_METHOD = 900;
    int FAILED_TO_PARSE_REQUEST = 901;
    int INADEQUATE_REQUEST = 902;
    int AUTHENTICATION_FAILED = 990;
    int UNCAUGHT = 999;

    static String getErrorDescription(final int errorCode) {
        switch (errorCode) {
            case NO_ERROR:
                return "Successful.";
            case USERNAME_EMPTY:
                return "The username cannot be empty.";
            case PASSWORD_EMPTY:
                return "The password cannot be empty.";
            case USERNAME_DOES_NOT_EXIST:
                return "The entered username does not match any accounts.";
            case USERNAME_ALREADY_EXISTS:
                return "The entered username already exists.";
            case BAD_USERNAME:
                return "The entered username is not a valid username.";
            case PASSWORD_TOO_WEAK:
                return "The password is not strong enough.";
            case INCORRECT_PASSWORD:
                return "The password you entered was incorrect.";
            case INVALID_CLIENT:
                return "The client is invalid.";
            default:
                return "No description is available for this error.";
        }
    }
}