package ir.arg.server;

import java.util.Collection;
import java.util.Date;

public interface Tweet extends StoredOnDisk{
    User getSender();
    String getContents();
    Date getSentOn();
    Collection<User> getLikes();
    default int getLikeCount() {
        return getLikes().size();
    }
}
