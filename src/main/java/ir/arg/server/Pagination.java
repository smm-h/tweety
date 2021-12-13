package ir.arg.server;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

public interface Pagination {
    void discardNext(final int count);

    JSONArray getNext(final int count);

    boolean isDepleted();
}
