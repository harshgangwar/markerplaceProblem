package com.marketplace.service;

import com.marketplace.model.*;
import com.marketplace.repository.*;
import com.marketplace.util.*;
import java.math.BigDecimal;
import java.util.*;

/** Application service: authentication checks and marketplace business rules. */
public final class MarketplaceService {
    private final UserRepository users;
    private final ListingRepository listings;
    private final CategoryStatsRepository categoryStats;
    private final IdGenerator idGenerator;
    private final TimeProvider clock;

    public MarketplaceService(UserRepository users, ListingRepository listings,
                              CategoryStatsRepository categoryStats, IdGenerator idGenerator,
                              TimeProvider clock) {
        this.users = users; this.listings = listings; this.categoryStats = categoryStats;
        this.idGenerator = idGenerator; this.clock = clock;
    }
    public String register(String username) {
        return users.add(new User(username)) ? "Success" : "Error - user already existing";
    }
    public String createListing(String username, String title, String description,
                                BigDecimal price, String category) {
        if (!users.exists(username)) return "Error - unknown user";
        long id = idGenerator.nextId();
        Listing listing = new Listing(id, title, description, price, username, category, clock.now());
        listings.save(listing);
        categoryStats.increment(category);
        return String.valueOf(id);
    }
    public String deleteListing(String username, long id) {
        Listing listing = listings.findById(id);
        if (listing == null) return "Error - listing does not exist";
        if (!sameUser(listing.getOwnerUsername(), username)) return "Error - listing owner mismatch";
        // Repository delete is the mutation boundary; stats update only after successful removal.
        Listing removed = listings.delete(id);
        if (removed != null) categoryStats.decrement(removed.getCategory());
        return "Success";
    }
    public String getListing(String username, long id) {
        if (!users.exists(username)) return "Error - unknown user";
        Listing listing = listings.findById(id);
        return listing == null ? "Error - not found" : formatFull(listing);
    }
    public List<String> getCategory(String username, String category, SortField field, SortOrder order) {
        if (!users.exists(username)) return Collections.singletonList("Error - unknown user");
        List<Listing> result = listings.findByCategory(category);
        if (result.isEmpty()) return Collections.singletonList("Error - category not found");
        Comparator<Listing> comparator = field == SortField.PRICE
            ? Comparator.comparing(Listing::getPrice).thenComparing(Listing::getCreatedAt)
            : Comparator.comparing(Listing::getCreatedAt).thenComparing(Listing::getId);
        if (order == SortOrder.DESC) comparator = comparator.reversed();
        result.sort(comparator);
        List<String> lines = new ArrayList<String>();
        for (Listing listing : result) lines.add(formatCategory(listing));
        return lines;
    }
    public String getTopCategory(String username) {
        if (!users.exists(username)) return "Error - unknown user";
        String category = categoryStats.topCategory();
        return category == null ? "Error - category not found" : category;
    }
    private boolean sameUser(String a, String b) { return a.equalsIgnoreCase(b); }
    private String formatFull(Listing l) {
        return l.getTitle()+"|"+l.getDescription()+"|"+price(l)+"|"+time(l)+"|"+l.getCategory()+"|"+l.getOwnerUsername();
    }
    private String formatCategory(Listing l) {
        return l.getTitle()+"|"+l.getDescription()+"|"+price(l)+"|"+time(l);
    }
    private String price(Listing l) { return l.getPrice().stripTrailingZeros().toPlainString(); }
    private String time(Listing l) {
        return java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(java.time.ZoneId.systemDefault()).format(l.getCreatedAt());
    }
}
