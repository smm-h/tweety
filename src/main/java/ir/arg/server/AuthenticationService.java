package ir.arg.server;

import org.jetbrains.annotations.NotNull;

public interface AuthenticationService {
    @NotNull
    SigningUpOutcome signUp(@NotNull final String username, @NotNull final String password);

    @NotNull
    SigningInOutcome signIn(@NotNull final String username, @NotNull final String password, final int signingInToken);

    enum SigningUpOutcome implements Outcome {
        SUCCESSFUL, USERNAME_ALREADY_EXISTS, RESERVED_USERNAME, BAD_USERNAME, USERNAME_EMPTY, PASSWORD_TOO_WEAK, PASSWORD_EMPTY;

        @Override
        public int getCode() {
            switch (this) {
                case SUCCESSFUL:
                    return 1;
                case USERNAME_EMPTY:
                    return 100;
                case BAD_USERNAME:
                    return 101;
                case USERNAME_ALREADY_EXISTS:
                    return 102;
                case RESERVED_USERNAME:
                    return 103;
                case PASSWORD_EMPTY:
                    return 200;
                case PASSWORD_TOO_WEAK:
                    return 201;
                default:
                    return 0;
            }
        }

        @Override
        public @NotNull String getDescription() {
            switch (this) {
                case SUCCESSFUL:
                    return "Signing up was successful.";
                case USERNAME_ALREADY_EXISTS:
                    return "The entered username already exists.";
                case RESERVED_USERNAME:
                    return "The entered username is reserved.";
                case BAD_USERNAME:
                    return "The entered username is not a valid username.";
                case USERNAME_EMPTY:
                    return "The username cannot be empty.";
                case PASSWORD_TOO_WEAK:
                    return "The password is not strong enough.";
                case PASSWORD_EMPTY:
                    return "The password cannot be empty.";
                default:
                    return "Unknown error occurred.";
            }
        }
    }

    enum SigningInOutcome implements Outcome {
        SUCCESSFUL, USERNAME_DOES_NOT_EXIST, BAD_USERNAME, USERNAME_EMPTY, INCORRECT_PASSWORD, PASSWORD_EMPTY;

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
                default:
                    return "Unknown error occurred.";
            }
        }
    }
}