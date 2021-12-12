package ir.arg.server.auth.impl;

import ir.arg.server.auth.PasswordStrengthService;

public class PasswordStrengthServiceImpl implements PasswordStrengthService {

    @Override
    public boolean isPasswordStrong(final String password) {
        return getPasswordStrength(password) > 0;
    }

    @Override
    public float getPasswordStrength(final String password) {
        if (password.length() >= 8) {
            boolean lower = false;
            boolean upper = false;
            boolean digit = false;
            boolean other = false;
            float strength = -6;
            for (char c : password.toCharArray()) {
                if (c >= 'a' && c <= 'z') {
                    lower = true;
                    strength += 0.7f;
                } else if (c >= 'A' && c <= 'Z') {
                    upper = true;
                    strength += 0.8f;
                } else if (c >= '0' && c <= '9') {
                    digit = true;
                    strength += 0.9f;
                } else if (c >= ' ' && c <= '~') {
                    other = true;
                    strength += 1.1f;
                } else {
                    other = true;
                    strength += 1.3f;
                }
            }
            return (lower && upper && digit && other) ? strength : 0;
        } else return 0;
    }
}
