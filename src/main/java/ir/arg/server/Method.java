package ir.arg.server;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface Method {
    @NotNull
    JSONObject process(@NotNull final Server server, @NotNull final JSONObject object);
}
