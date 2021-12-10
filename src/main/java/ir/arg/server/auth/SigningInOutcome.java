package ir.arg.server.auth;

import ir.arg.server.Outcome;
import org.jetbrains.annotations.NotNull;

public enum SigningInOutcome implements Outcome {
    SUCCESSFUL, USERNAME_DOES_NOT_EXIST, BAD_USERNAME, USERNAME_EMPTY, INCORRECT_PASSWORD, PASSWORD_EMPTY, EMPTY_TOKEN;

    @Override
    public int getCode() {
        switch (this) {
            case SUCCESSFUL:
                return 1;
            case USERNAME_EMPTY:
                return 100;
            case USERNAME_DOES_NOT_EXIST:
                return 101;
            case BAD_USERNAME:
                return 102;
            case PASSWORD_EMPTY:
                return 200;
            case INCORRECT_PASSWORD:
                return 201;
            case EMPTY_TOKEN:
                return 300;
            default:
                return 0;
        }
    }

    @Override
    public @NotNull String getDescription() {
        switch (this) {
            case SUCCESSFUL:
                return "Signing in was successful.";
            case USERNAME_DOES_NOT_EXIST:
                return "The entered username does not match any accounts.";
            case BAD_USERNAME:
                return "The entered username is not a valid username.";
            case USERNAME_EMPTY:
                return "The username cannot be empty.";
            case INCORRECT_PASSWORD:
                return "The password you entered was incorrect.";
            case PASSWORD_EMPTY:
                return "The password cannot be empty.";
            case EMPTY_TOKEN:
                return "The client is invalid.";
            default:
                return "Unknown error occurred.";
        }
    }
}
