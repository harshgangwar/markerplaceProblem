package com.marketplace.model;

import java.math.BigDecimal;
import java.time.Instant;

/** Immutable listing aggregate. */
public final class Listing {
    private final long id;
    private final String title;
    private final String description;
    private final BigDecimal price;
    private final String ownerUsername;
    private final String category;
    private final Instant createdAt;

    public Listing(long id, String title, String description, BigDecimal price,
                   String ownerUsername, String category, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.ownerUsername = ownerUsername;
        this.category = category;
        this.createdAt = createdAt;
    }
    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public String getOwnerUsername() { return ownerUsername; }
    public String getCategory() { return category; }
    public Instant getCreatedAt() { return createdAt; }
}
