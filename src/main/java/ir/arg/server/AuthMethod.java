package ir.arg.server;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import static ir.arg.server.shared.ErrorCode.*;

public interface AuthMethod extends Method {

    @Override
    default @NotNull JSONObject process(final @NotNull Server server, final @NotNull JSONObject object) {
        final User me;
        try {
            final String myUsername = object.getString("my_username");
            final String token = object.getString("token");
            if (server.getAuthenticationService().authenticate(myUsername, token)) {
                me = server.getUserStorage().findUser(myUsername);
                if (me == null) {
                    return ServerAPI.err(USER_NOT_FOUND);
                } else {
                    return process2(server, me, object);
                }
            } else {
                return ServerAPI.err(AUTHENTICATION_FAILED);
            }
        } catch (JSONException e) {
            return ServerAPI.err(UNAUTHORIZED_REQUEST, e);
        }
    }

    @NotNull
    JSONObject process2(@NotNull final Server server, @NotNull final User user, @NotNull final JSONObject object);
}
