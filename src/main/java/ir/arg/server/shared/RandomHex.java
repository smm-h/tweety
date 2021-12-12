package ir.arg.server.shared;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface RandomHex {

    Random random = new Random();

    @NotNull
    static String generate(final int length) {
        final char[] array = new char[length];
        for (int i = 0; i < length; i++) {
            final int x = random.nextInt(16);
            array[i] = (char) (x < 10 ? '0' + x : 'a' + x - 10);
        }
        return new String(array);
    }
}
