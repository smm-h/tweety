package ir.arg.server.auth.impl;

import ir.arg.server.auth.SignInBundle;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONSignInBundleImpl implements SignInBundle {

    @NotNull
    private final JSONObject object;

    public JSONSignInBundleImpl(@NotNull JSONObject object) {
        this.object = object;
    }

    @Override
    public @NotNull String getEnteredUsername() {
        try {
            return object.getString("username");
        } catch (JSONException e) {
            return "";
        }
    }

    @Override
    public @NotNull String getEnteredPassword() {
        try {
            return object.getString("password");
        } catch (JSONException e) {
            return "";
        }
    }

    @Override
    public @NotNull String getGeneratedToken() {
        try {
            return object.getString("token");
        } catch (JSONException e) {
            return "";
        }
    }
}
