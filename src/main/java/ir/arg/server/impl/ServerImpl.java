package ir.arg.server.impl;

import ir.arg.server.App;
import ir.arg.server.Server;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerImpl implements Server {

    public static void main(String[] args) {
        new ServerImpl().start();
    }

    @Override
    public int getDefaultPort() {
        return 7000;
    }

    private static Server instance = null;

    @Override
    public void stop() {
        instance = null;
    }

    @Override
    public void start(int port) {
        if (instance == null) {
            instance = this;
            System.out.println("Server started...");
            try {
                final ServerSocket serverSocket = new ServerSocket(port);
                while (instance != null)
                    new RequestHandler(serverSocket.accept()).start();
            } catch (IOException e) {
                System.err.println("Failed to keep the server running");
            }
            System.out.println("Server shutting down...");
            instance = null;
        }
    }

    private static @NotNull String request(final @NotNull String request) {
        final App app = App.getInstance();
        app.log();
        app.log("REQUEST: " + request);
        final String response = app.request(request);
        app.log("RESPONSE: " + response);
        return response;
    }

    private static class RequestHandler extends Thread {

        private final Socket socket;

        public RequestHandler(@NotNull final Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            final DataInputStream req;
            final DataOutputStream res;
            try {
                req = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                res = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            } catch (IOException e) {
                System.err.println("Failed to communicate with the socket");
                return;
            }
            final String request;
            try {
                request = req.readUTF();
                System.out.println(request);
                final String response;
                try {
                    response = request(request);
                    System.out.println(response);
                    try {
                        res.writeUTF(response);
                    } catch (IOException e) {
                        System.err.println("Failed to write response");
                    }
                } catch (Throwable e) {
                    System.err.println("Failed to process request");
                }
            } catch (IOException e) {
                System.err.println("Failed to read request");
            }
            try {
                res.close();
                req.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("Failed to close socket");
            }
        }
    }
}
