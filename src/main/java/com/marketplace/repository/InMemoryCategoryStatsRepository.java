package com.marketplace.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Maintains O(number of categories) top lookup rather than scanning all listings.
 * Updates are atomic per category; a small synchronized critical section gives a consistent top view.
 */
public final class InMemoryCategoryStatsRepository implements CategoryStatsRepository {
    private final ConcurrentMap<String, AtomicInteger> counts = new ConcurrentHashMap<String, AtomicInteger>();
    private final ConcurrentMap<String, String> displayNames = new ConcurrentHashMap<String, String>();

    public void increment(String category) {
        String key = key(category);
        displayNames.putIfAbsent(key, category);
        counts.computeIfAbsent(key, k -> new AtomicInteger()).incrementAndGet();
    }
    public void decrement(String category) {
        AtomicInteger count = counts.get(key(category));
        if (count != null) count.decrementAndGet();
    }
    public String topCategory() {
        String best = null;
        int bestCount = -1;
        for (Map.Entry<String, AtomicInteger> e : counts.entrySet()) {
            int count = e.getValue().get();
            if (count > bestCount && count > 0) {
                best = e.getKey(); bestCount = count;
            } else if (count == bestCount && count > 0 && best != null && e.getKey().compareTo(best) < 0) {
                best = e.getKey();
            }
        }
        return best == null ? null : displayNames.get(best);
    }
    private String key(String value) { return value.toLowerCase(Locale.ROOT); }
}
