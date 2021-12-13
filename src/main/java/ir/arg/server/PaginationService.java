package ir.arg.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A pagination service offers three services: 1) a way to assign unique IDs to given paginations,
 * 2) a way to find paginations using their unique IDs, and 3) a guarantee that paginations that remain
 * unused for a while will get removed so as to avoid memory abuse, as paginations are expected to be
 * created very frequently.
 */
public interface PaginationService {

    @Nullable Pagination find(@NotNull String paginationId);

    @NotNull String add(@NotNull Pagination pagination);

}
