package ir.arg.server;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface AuthMethod {
    @NotNull
    JSONObject process(@NotNull final Server server, @NotNull final User user, @NotNull final JSONObject object);
}
