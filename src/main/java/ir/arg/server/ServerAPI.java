package ir.arg.server;

import ir.arg.server.auth.impl.JSONSignInBundleImpl;
import ir.arg.server.auth.impl.JSONSignUpBundleImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ServerAPI implements ErrorCode {

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

    private JSONObject err(final int errorCode) {
        // TODO logging
        final JSONObject response = new JSONObject();
        response.put("error_code", errorCode);
        response.put("description", ErrorCode.getErrorDescription(errorCode));
        return response;
    }

    private JSONObject err(final int errorCode, final Throwable error) {
        final JSONObject response = err(errorCode);
        response.put("message", error.getMessage());
        error.printStackTrace();
        return response;
    }

    public String request(final String request) {
        try {
            final JSONObject object = safeParse(request);
            if (object == null) {
                return err(FAILED_TO_PARSE_REQUEST).toString();
            } else {
                try {
                    // TODO max len 256
                    switch (object.getString("method")) {
                        case "sign_in":
                            return err(server.getAuthenticationService().signIn(new JSONSignInBundleImpl(object.getJSONObject("sign_in_bundle")))).toString();
                        case "sign_up":
                            return err(server.getAuthenticationService().signUp(new JSONSignUpBundleImpl(object.getJSONObject("sign_up_bundle")))).toString();
//                        case "send_tweet": {
//                            final Outcome outcome = server.getTweetingService().sendTweet();
//                            response.put("error_code", outcome.getCode());
//                            response.put("description", outcome.getDescription());
//                        }
//                        break;
                        default:
                            return err(UNDEFINED_METHOD).toString();
                    }
                } catch (JSONException e) {
                    return err(INADEQUATE_REQUEST, e).toString();
                }
            }
        } catch (Throwable e) {
            return err(UNCAUGHT, e).toString();
        }
    }
}
