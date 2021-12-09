package ir.arg.server;
//import org.jetbrains.annotations.NonNull; TODO
public interface Server {


    static Server getServer() {
        return null;
    }

    Properties getProperties();
}
