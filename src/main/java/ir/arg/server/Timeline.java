package ir.arg.server;

import ir.arg.server.impl.TimelineImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Set;

public interface Timeline extends Pagination<JSONObject> {
    @Nullable
    static Timeline of(@NotNull final Set<User> users) {
        return users.size() == 0 ? null : new TimelineImpl(users);
    }
    static @NotNull Timeline of(@NotNull final User user) {
        return new TimelineImpl(user);
    }
}
