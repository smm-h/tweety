package ir.arg.server;

import ir.arg.server.impl.ServerImpl;
import org.jetbrains.annotations.NotNull;

public class ServerSingleton {

    private ServerSingleton() {
    }

    private static Server SERVER = null;

    @NotNull
    public static Server getServer() {
        return SERVER != null ? SERVER : (SERVER = new ServerImpl());
    }

}
