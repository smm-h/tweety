package ir.arg.server.impl;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class JSONHelper {
    @NotNull
    static Set<String> getStringSet() {
        return new LinkedHashSet<>();
    }

    @NotNull
    static Set<String> getStringSet(@NotNull final String... array) {
        final Set<String> set = getStringSet();
        set.addAll(Arrays.asList(array));
        return set;
    }

    @NotNull
    static Set<String> getStringSet(@NotNull final JSONArray array) {
        final Set<String> set = getStringSet();
        for (int i = 0; i < array.length(); i++)
            set.add(array.getString(i));
        return set;
    }
}
