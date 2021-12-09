package ir.arg.server;

public interface StoredOnDisk {
    String getFilename();

    boolean isOnDisk();

    String getData();

    void setData(String data);

    default void writeDataToDisk() {
        // TODO
    }

    default String readDataFromDisk() {
        // TODO
        return null;
    }
}
