package ir.arg.server;

import ir.arg.server.shared.APIMethods;
import ir.arg.server.shared.ErrorCode;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ServerAPI implements ErrorCode, APIMethods {

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

    public static JSONObject err(final int errorCode) {
        final JSONObject response = new JSONObject();
        response.put("error_code", errorCode);
        getInstance().server.log("ERROR: " + errorCode + " (" + ErrorCode.getErrorDescription(errorCode) + ")");
//        response.put("description", ErrorCode.getErrorDescription(errorCode));
        return response;
    }

    public static JSONObject err(final int errorCode, final Throwable error) {
        final JSONObject response = err(errorCode);
        getInstance().server.log("DETAILS: " + error.getMessage());
        error.printStackTrace();
        return response;
    }

    public String request(final String request) {
        server.log("REQUEST: " + request);
        try {
            final JSONObject object = safeParse(request);
            if (object == null) {
                return err(FAILED_TO_PARSE_REQUEST).toString();
            } else {
                final String method;
                try {
                    method = object.getString("method");
                } catch (JSONException e) {
                    return err(METHOD_MISSING, e).toString();
                }
                if (APIMethods.isAuthenticationRequired(method)) {
                    final User me;
                    try {
                        final String myUsername = object.getString("my_username");
                        final String token = object.getString("token");
                        if (server.getAuthenticationService().authenticate(myUsername, token)) {
                            me = server.getUserStorage().findUser(myUsername);
                        } else {
                            return err(AUTHENTICATION_FAILED).toString();
                        }
                    } catch (JSONException e) {
                        return err(UNAUTHORIZED_REQUEST, e).toString();
                    }
                    switch (method) {
                        case CREATE_TWEET:
                            return err(server.getTweetingService().createTweet(me, object)).toString();
                        default:
                            return err(UNDEFINED_METHOD).toString();
                    }
                } else {
                    switch (method) {
                        case SIGN_UP:
                            return err(server.getAuthenticationService().signUp(object)).toString();
                        case SIGN_IN:
                            return err(server.getAuthenticationService().signIn(object)).toString();
                        default:
                            return err(UNDEFINED_METHOD).toString();
                    }
                }
            }
        } catch (Throwable e) {
            return err(UNCAUGHT, e).toString();
        }
    }
}
