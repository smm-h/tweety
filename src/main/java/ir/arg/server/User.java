package ir.arg.server;

public interface User extends StoredOnDisk {
    String getUsername();
    String getName();
    String getBio();
    String getPasswordHash();
}
