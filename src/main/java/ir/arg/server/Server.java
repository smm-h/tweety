package ir.arg.server;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public interface Server {

    @NotNull
    static Server getServer() {
        return null;
    }

    Properties getProperties();

    Database getUserDatabase();

    Database getTweetDatabase();

    UserStorage getUserStorage();

    DateFormat getDateFormat();

    // DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

}
