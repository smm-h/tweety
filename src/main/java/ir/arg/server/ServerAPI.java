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
            final JSONObject object;
            try {
                object = new JSONObject(new JSONTokener(request));
            } catch (JSONException e) {
                return err(FAILED_TO_PARSE_REQUEST).toString();
            }
            final String method;
            try {
                method = object.getString("method");
            } catch (JSONException e) {
                return err(METHOD_MISSING, e).toString();
            }
            switch (method) {
                case SIGN_UP:
                    return Methods.signUp.process(server, object).toString();
                case SIGN_IN:
                    return Methods.signIn.process(server, object).toString();
                case CREATE_TWEET:
                    return Methods.createTweet.process(server, object).toString();
                default:
                    return err(UNDEFINED_METHOD).toString();
            }
        } catch (Throwable e) {
            return err(UNCAUGHT, e).toString();
        }
    }
}
