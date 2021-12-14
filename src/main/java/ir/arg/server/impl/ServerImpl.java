package ir.arg.server.impl;

import ir.arg.server.App;
import ir.arg.server.Server;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerImpl implements Server {

    public static void main(String[] args) {
        new ServerImpl().listen();
    }

    @Override
    public int getDefaultPort() {
        return 7000;
    }

    @Override
    public void listen(int port) {
        System.out.println("Server started...");
        try {
            final ServerSocket server = new ServerSocket(port);
            final Socket socket = server.accept();
            final DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String line = "";
            while (!line.equals(".")) {
                try {
                    line = in.readUTF();
                    System.out.println(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static @NotNull String request(final @NotNull String request) {
        final App app = App.getInstance();
        app.log();
        app.log("REQUEST: " + request);
        final String response = app.request(request);
        app.log("RESPONSE: " + response);
        return response;
    }
}
