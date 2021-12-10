package ir.arg.server;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public interface JSONSerializable {
    @NotNull
    JSONObject serialize();
}
