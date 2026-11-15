package com.marketplace.repository;

/** Read-optimized aggregate/index for GET_TOP_CATEGORY. */
public interface CategoryStatsRepository {
    void increment(String category);
    void decrement(String category);
    String topCategory();
}
