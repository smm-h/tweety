package ir.arg.client;

import ir.arg.server.Server;
import ir.arg.server.ServerSingleton;
import ir.arg.server.auth.impl.SimpleSignInBundleImpl;
import ir.arg.server.auth.impl.SimpleSignUpBundleImpl;

public class App {

    public static void main(String[] args) {
        Server server = ServerSingleton.getServer();
        server.getAuthenticationService().signUp(new SimpleSignUpBundleImpl("arg", "1234"));
        server.getAuthenticationService().signIn(new SimpleSignInBundleImpl("arg", "12345", ""));
    }
}
