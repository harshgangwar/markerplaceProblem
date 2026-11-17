
package com.marketplace.service;

import com.marketplace.model.*;
import com.marketplace.repository.*;
import com.marketplace.util.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class MarketplaceServiceTest {

    private MarketplaceService service;

    public void setUp() {
        service = new MarketplaceService(
                new InMemoryUserRepository(),
                new InMemoryListingRepository(),
                new InMemoryCategoryStatsRepository(),
                new IdGenerator(),
                new TimeProvider() {
                    @Override
                    public Instant now() {
                        return Instant.parse("2026-01-01T10:00:00Z");
                    }
                }
        );
    }

    public void testUserRegistration() {
        setUp();

        assertEquals("Success", service.register("harsh"));
        assertEquals(
                "Error - user already existing",
                service.register("HARSH")
        );
    }

    public void testCreateListing() {
        setUp();

        service.register("harsh");

        String id = service.createListing(
                "harsh",
                "iPhone 15",
                "Brand new",
                new BigDecimal("50000"),
                "Electronics"
        );

        assertEquals("100001", id);
    }

    public void testCreateListingForUnknownUser() {
        setUp();

        assertEquals(
                "Error - unknown user",
                service.createListing(
                        "unknown",
                        "iPhone",
                        "New",
                        new BigDecimal("50000"),
                        "Electronics"
                )
        );
    }

    public void testGetListing() {
        setUp();

        service.register("harsh");
        service.register("user2");

        service.createListing(
                "harsh", "iPhone", "New",
                new BigDecimal("50000"), "Electronics"
        );

        String result = service.getListing("user2", 100001);

        assertTrue(result.contains("iPhone"));
        assertTrue(result.contains("Electronics"));
        assertTrue(result.contains("harsh"));
    }

    public void testGetListingForUnknownUser() {
        setUp();

        assertEquals(
                "Error - unknown user",
                service.getListing("unknown", 100001)
        );
    }

    public void testGetNonExistingListing() {
        setUp();

        service.register("harsh");

        assertEquals(
                "Error - not found",
                service.getListing("harsh", 999999)
        );
    }

    public void testDeleteListingByOwner() {
        setUp();

        service.register("harsh");

        service.createListing(
                "harsh", "iPhone", "New",
                new BigDecimal("50000"), "Electronics"
        );

        assertEquals(
                "Success",
                service.deleteListing("harsh", 100001)
        );

        assertEquals(
                "Error - not found",
                service.getListing("harsh", 100001)
        );
    }

    public void testDeleteListingByNonOwner() {
        setUp();

        service.register("harsh");
        service.register("user2");

        service.createListing(
                "harsh", "iPhone", "New",
                new BigDecimal("50000"), "Electronics"
        );

        assertEquals(
                "Error - listing owner mismatch",
                service.deleteListing("user2", 100001)
        );
    }

    public void testDeleteNonExistingListing() {
        setUp();

        service.register("harsh");

        assertEquals(
                "Error - listing does not exist",
                service.deleteListing("harsh", 999999)
        );
    }

    public void testGetCategorySortedByPrice() {
        setUp();

        service.register("harsh");

        service.createListing(
                "harsh", "iPhone", "New",
                new BigDecimal("50000"), "Electronics"
        );

        service.createListing(
                "harsh", "Laptop", "New",
                new BigDecimal("80000"), "Electronics"
        );

        List<String> result = service.getCategory(
                "harsh",
                "Electronics",
                SortField.PRICE,
                SortOrder.ASC
        );

        assertEquals(2, result.size());

        assertTrue(result.get(0).startsWith("iPhone"));
        assertTrue(result.get(1).startsWith("Laptop"));
    }

    public void testGetCategoryNotFound() {
        setUp();

        service.register("harsh");

        List<String> result = service.getCategory(
                "harsh",
                "Fashion",
                SortField.PRICE,
                SortOrder.ASC
        );

        assertEquals(1, result.size());
        assertEquals("Error - category not found", result.get(0));
    }

    public void testTopCategory() {
        setUp();

        service.register("harsh");

        service.createListing(
                "harsh", "iPhone", "New",
                new BigDecimal("50000"), "Electronics"
        );

        service.createListing(
                "harsh", "Shoes", "New",
                new BigDecimal("2000"), "Sports"
        );

        service.createListing(
                "harsh", "Football", "New",
                new BigDecimal("1000"), "Sports"
        );

        assertEquals("Sports", service.getTopCategory("harsh"));
    }

    public void testTopCategoryAfterDeletion() {
        setUp();

        service.register("harsh");

        service.createListing(
                "harsh", "iPhone", "New",
                new BigDecimal("50000"), "Electronics"
        );

        service.createListing(
                "harsh", "Shoes", "New",
                new BigDecimal("2000"), "Sports"
        );

        service.createListing(
                "harsh", "Football", "New",
                new BigDecimal("1000"), "Sports"
        );

        service.deleteListing("harsh", 100002);
        service.deleteListing("harsh", 100003);

        assertEquals("Electronics", service.getTopCategory("harsh"));
    }

    public void testGetTopCategoryUnknownUser() {
        setUp();

        assertEquals(
                "Error - unknown user",
                service.getTopCategory("unknown")
        );
    }

    // ---------------- CUSTOM ASSERTIONS ----------------

    private void assertEquals(Object expected, Object actual) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(
                    "Expected: " + expected + ", Actual: " + actual
            );
        }
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected condition to be true");
        }
    }
}
