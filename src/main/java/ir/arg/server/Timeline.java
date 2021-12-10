package ir.arg.server;

import ir.arg.server.impl.TimelineImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface Timeline extends Pagination {
    @Nullable
    static Timeline of(@NotNull final Set<User> users) {
        return users.size() == 0 ? null : new TimelineImpl(users);
    }
}
