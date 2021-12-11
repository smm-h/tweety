package ir.arg.client.requests;

import org.json.JSONObject;

public class RestrictionException extends Exception {
    public RestrictionException(final String message) {
        super(message);
    }

    public static String restrictUsername(String username) throws RestrictionException {
        username = JSONObject.quote(username.trim());
        if (username.length() == 0) {
            throw new RestrictionException("username cannot be empty");
        } else if (username.length() > 256) {
            throw new RestrictionException("username cannot be more than 256 characters long");
        } else if (username.isBlank()) {
            throw new RestrictionException("username cannot be blank");
        } else {
            return username;
        }
    }

    public static String restrictName(String name) throws RestrictionException {
        name = JSONObject.quote(name.trim());
        if (name.length() > 64) {
            throw new RestrictionException("name cannot be more than 64 characters long");
        } else {
            return name;
        }
    }

    public static String restrictBio(String bio) throws RestrictionException {
        bio = JSONObject.quote(bio.trim());
        if (bio.length() > 256) {
            throw new RestrictionException("bio cannot be more than 256 characters long");
        } else {
            return bio;
        }
    }

    public static String restrictTweetContents(String contents) throws RestrictionException {
        contents = JSONObject.quote(contents.trim());
        if (contents.length() == 0) {
            throw new RestrictionException("tweet content cannot be empty");
        } else if (contents.length() > 256) {
            throw new RestrictionException("tweet content cannot be more than 256 characters long");
        } else if (contents.isBlank()) {
            throw new RestrictionException("tweet content cannot be blank");
        } else {
            return contents;
        }
    }
}
