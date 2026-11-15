package com.marketplace.repository;
import com.marketplace.model.Listing;
import java.util.List;

public interface ListingRepository {
    void save(Listing listing);
    Listing findById(long id);
    Listing delete(long id);
    List<Listing> findByCategory(String category);
}
