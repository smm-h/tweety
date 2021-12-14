package ir.arg.server;

public interface Server {

    int getDefaultPort();

    default void listen() {
        listen(getDefaultPort());
    }

    void listen(int port);
}
