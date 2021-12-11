package ir.arg.server;

public interface ErrorCode {
    int NO_ERROR = 0;
    int USERNAME_EMPTY = 101100;
    int PASSWORD_EMPTY = 101200;
    int USERNAME_DOES_NOT_EXIST = 101101;
    int BAD_USERNAME = 101102;
    int INCORRECT_PASSWORD = 101201;
    int EMPTY_TOKEN = 101300;
    int USERNAME_ALREADY_EXISTS = 102102;
    int PASSWORD_TOO_WEAK = 102201;

    static String getErrorDescription(final int errorCode) {
        switch (errorCode) {
            case 0:
                return "Successful.";
            case 101100:
                return "The entered username does not match any accounts.";
            case 101101:
                return "The entered username is not a valid username.";
            case 101102:
                return "The username cannot be empty.";
            case 101200:
                return "The password you entered was incorrect.";
            case 101201:
                return "The password cannot be empty.";
            case 101300:
                return "The client is invalid.";
            case 102100:
                return "The entered username already exists.";
            case 102200:
                return "The password is not strong enough.";
            default:
                return "Unknown error occurred.";
        }
    }
}