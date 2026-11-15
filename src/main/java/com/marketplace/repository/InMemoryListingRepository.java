package com.marketplace.repository;

import com.marketplace.model.Listing;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** Thread-safe primary store plus category index. */
public final class InMemoryListingRepository implements ListingRepository {
    private final ConcurrentMap<Long, Listing> byId = new ConcurrentHashMap<Long, Listing>();
    private final ConcurrentMap<String, Set<Long>> idsByCategory = new ConcurrentHashMap<String, Set<Long>>();

    public void save(Listing listing) {
        byId.put(listing.getId(), listing);
        Set<Long> ids = idsByCategory.computeIfAbsent(key(listing.getCategory()),
            k -> ConcurrentHashMap.newKeySet());
        ids.add(listing.getId());
    }
    public Listing findById(long id) { return byId.get(id); }
    public Listing delete(long id) {
        Listing removed = byId.remove(id);
        if (removed != null) {
            Set<Long> ids = idsByCategory.get(key(removed.getCategory()));
            if (ids != null) ids.remove(id);
        }
        return removed;
    }
    public List<Listing> findByCategory(String category) {
        Set<Long> ids = idsByCategory.get(key(category));
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<Listing> result = new ArrayList<Listing>();
        for (Long id : ids) {
            Listing l = byId.get(id);
            if (l != null) result.add(l);
        }
        return result;
    }
    private String key(String value) { return value.toLowerCase(Locale.ROOT); }
}
