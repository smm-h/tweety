package ir.arg.server.impl;

import ir.arg.server.Pagination;
import ir.arg.server.PaginationService;
import ir.arg.server.shared.RandomHex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class PaginationServiceImpl implements PaginationService {

    private final Map<String, Pagination> paginationPool = new HashMap<>();
    private final Map<String, Instant> paginationLastUsed = new HashMap<>();

    @Override
    public @Nullable Pagination findPagination(@NotNull String paginationId) {
        if (paginationPool.containsKey(paginationId)) {
            paginationLastUsed.put(paginationId, Instant.now());
            return paginationPool.get(paginationId);
        } else {
            return null;
        }
    }

    @Override
    public @NotNull String identifyPagination(@NotNull Pagination pagination) {
        final String paginationId = RandomHex.generate(32);
        paginationPool.put(paginationId, pagination);
        paginationLastUsed.put(paginationId, Instant.now());
        return paginationId;
    }
}
