package ir.arg.client;

import ir.arg.server.ServerAPI;

public class ClientApp {
    private final ServerAPI api = ServerAPI.getInstance();

    public ClientApp() {
        request("{\"action\": \"sign_up\"}, \"bundle\": {\"username\": \"arg\", \"password\": \"abcDEF123!@#\"}");
    }

    public static void main(String[] args) {
        new ClientApp();
//        System.out.print("Hey-o!");
    }

    private void request(final String json) {
        System.out.print(api.request(json));
    }
}
