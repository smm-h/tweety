package ir.arg.server;

public interface Server {

    int getDefaultPort();

    default void start() {
        start(getDefaultPort());
    }

    void start(int port);

    void stop();
}
