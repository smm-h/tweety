package ir.arg.server.impl;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.LinkedHashSet;
import java.util.Set;

public class JSONHelper {
    @NotNull
    static Set<String> getStringSet(@NotNull final JSONArray array) {
        final Set<String> set = new LinkedHashSet<>();
        for (int i = 0; i < array.length(); i++)
            set.add(array.getString(i));
//        for (Object i : array)
//            set.add((String) i);
        return set;
    }
}
