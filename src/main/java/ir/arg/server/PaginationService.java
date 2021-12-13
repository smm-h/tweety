package ir.arg.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PaginationService {

    @Nullable Pagination findPagination(@NotNull String paginationId);

    @NotNull String identifyPagination(@NotNull Pagination pagination);

}
