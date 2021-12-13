package ir.arg.server;

import ir.arg.server.impl.ServerImpl;
import org.jetbrains.annotations.NotNull;

public class ServerSingleton {

    private ServerSingleton() {
    }

    private static Server server = null;

    @NotNull
    public static Server getServer() {
        return server != null ? server : (server = new ServerImpl());
    }

    @NotNull
    public static String request(@NotNull final String request) {
        if (server == null)
            server = new ServerImpl();
        server.log();
        server.log("REQUEST: " + request);
        final String response = server.request(request);
        server.log("RESPONSE: " + response);
        return response;
    }
}
