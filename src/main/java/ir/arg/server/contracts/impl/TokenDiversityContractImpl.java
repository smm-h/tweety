package ir.arg.server.contracts.impl;

import ir.arg.server.contracts.TokenDiversityContract;
import org.jetbrains.annotations.NotNull;

public class TokenDiversityContractImpl implements TokenDiversityContract {
    @Override
    public boolean verify(@NotNull String data) {
        if (data.length() >= 16) {
            int diversity = 0;
            boolean[] contains = new boolean[16];
            for (char c : data.toCharArray()) {
                final int i;
                if (c >= '0' && c <= '9') {
                    i = c - '0';
                } else if (c >= 'a' && c <= 'f') {
                    i = c - 'a' + 10;
                } else return false;
                if (!contains[i]) {
                    contains[i] = true;
                    diversity++;
                }
            }
            return diversity >= 12;
        } else return false;
    }
}
