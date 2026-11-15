package com.marketplace.cli;

import com.marketplace.model.*;
import com.marketplace.service.MarketplaceService;
import java.math.BigDecimal;
import java.util.*;

/** Thin command adapter: CLI concerns stay outside domain services. */
public final class CommandDispatcher {
    private final MarketplaceService service;
    private final CommandParser parser = new CommandParser();
    public CommandDispatcher(MarketplaceService service) { this.service = service; }

    public List<String> execute(String line) {
        try {
            List<String> t = parser.parse(line);
            if (t.isEmpty()) return Collections.emptyList();
            String command = t.get(0);
            if ("REGISTER".equals(command) && t.size() == 2)
                return one(service.register(t.get(1)));
            if ("CREATE_LISTING".equals(command) && t.size() == 6)
                return one(service.createListing(t.get(1), t.get(2), t.get(3), new BigDecimal(t.get(4)), t.get(5)));
            if ("DELETE_LISTING".equals(command) && t.size() == 3)
                return one(service.deleteListing(t.get(1), Long.parseLong(t.get(2))));
            if ("GET_LISTING".equals(command) && t.size() == 3)
                return one(service.getListing(t.get(1), Long.parseLong(t.get(2))));
            if ("GET_CATEGORY".equals(command) && t.size() == 5)
                return service.getCategory(t.get(1), t.get(2), parseField(t.get(3)), parseOrder(t.get(4)));
            if ("GET_TOP_CATEGORY".equals(command) && t.size() == 2)
                return one(service.getTopCategory(t.get(1)));
            return one("Error - invalid command");
        } catch (Exception e) { return one("Error - invalid command"); }
    }
    private List<String> one(String s) { return Collections.singletonList(s); }
    private SortField parseField(String s) {
        if ("sort_price".equals(s)) return SortField.PRICE;
        if ("sort_time".equals(s)) return SortField.TIME;
        throw new IllegalArgumentException();
    }
    private SortOrder parseOrder(String s) {
        if ("asc".equals(s)) return SortOrder.ASC;
        if ("dsc".equals(s)) return SortOrder.DESC;
        throw new IllegalArgumentException();
    }
}
