package ir.arg.server;

import ir.arg.server.auth.impl.JSONSignInBundleImpl;
import ir.arg.server.auth.impl.JSONSignUpBundleImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ServerAPI {

    private final Server server = ServerSingleton.getServer();

    private ServerAPI() {
    }

    private static ServerAPI instance = null;

    public static ServerAPI getInstance() {
        return instance != null ? instance : (instance = new ServerAPI());
    }

    public static void main(String[] args) {
        new ServerAPI();
    }

    private JSONObject safeParse(final String json) {
        try {
            return new JSONObject(new JSONTokener(json));
        } catch (JSONException e) {
            return null;
        }
    }

    public String request(final String request) {
        final JSONObject response = new JSONObject();
        try {
            final JSONObject object = safeParse(request);
            if (object == null) {
                response.put("error_code", 901);
                response.put("description", "Failed to parse request.");
            } else {
                try {
                    switch (object.getString("action")) {
                        case "sign_in": {
                            final int ec = server.getAuthenticationService().signIn(new JSONSignInBundleImpl(object.getJSONObject("sign_in_bundle")));
                            response.put("error_code", ec);
                            response.put("description", ErrorCode.getErrorDescription(ec));
                        }
                        break;
                        case "sign_up": {
                            final int ec = server.getAuthenticationService().signUp(new JSONSignUpBundleImpl(object.getJSONObject("sign_up_bundle")));
                            response.put("error_code", ec);
                            response.put("description", ErrorCode.getErrorDescription(ec));
                        }
                        break;
//                        case "send_tweet": {
//                            final Outcome outcome = server.getTweetingService().sendTweet();
//                            response.put("error_code", outcome.getCode());
//                            response.put("description", outcome.getDescription());
//                        }
//                        break;
                        default: {
                            response.put("error_code", 900);
                            response.put("description", "Undefined action.");
                        }
                        break;
                    }
                } catch (JSONException e) {
                    response.put("error_code", 902);
                    response.put("description", "Inadequate request");
                    response.put("message", e.getMessage());
                }
            }
        } catch (Error error) {
            response.put("error_code", 999);
            response.put("description", "Unknown error.");
            error.printStackTrace();
            // TODO logging
            // TODO max len 256
        }
        return response.toString();
    }
}
