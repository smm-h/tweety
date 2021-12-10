package ir.arg.server.auth;

import ir.arg.server.Outcome;
import ir.arg.server.Server;
import ir.arg.server.ServerSingleton;
import ir.arg.server.auth.impl.JSONSignInBundleImpl;
import ir.arg.server.auth.impl.JSONSignUpBundleImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class App {

    private final Server server;

    public App() {
        server = ServerSingleton.getServer();
    }

    public static void main(String[] args) {
        new App();
    }

    public String request(String request) {
        final JSONObject response = new JSONObject();
        try {
            final JSONObject object;
            try {
                object = new JSONObject(new JSONTokener(request));
            } catch (JSONException e) {
                response.put("error_code", 901);
                response.put("description", "Failed to parse request.");
                return response.toString();
            }
            switch (object.getString("action")) {
                case "sign_in": {
                    final Outcome outcome = server.getAuthenticationService().signIn(new JSONSignInBundleImpl(object.getJSONObject("sign_in_bundle")));
                    response.put("error_code", outcome.getCode());
                    response.put("description", outcome.getDescription());
                }
                break;
                case "sign_up": {
                    final Outcome outcome = server.getAuthenticationService().signUp(new JSONSignUpBundleImpl(object.getJSONObject("sign_up_bundle")));
                    response.put("error_code", outcome.getCode());
                    response.put("description", outcome.getDescription());
                }
                case "send_tweet": {
                    final Outcome outcome = server.getTweetingService().sendTweet();
                    response.put("error_code", outcome.getCode());
                    response.put("description", outcome.getDescription());
                }
                break;
                default: {
                    response.put("error_code", 900);
                    response.put("description", "Undefined action.");
                }
                break;
            }
        } catch (Error error) {
            response.put("error_code", 999);
            response.put("description", "Unknown error.");
            error.printStackTrace();
            // TODO logging
        }
        return response.toString();
    }
}
