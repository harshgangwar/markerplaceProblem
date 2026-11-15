package com.marketplace.app;

import com.marketplace.cli.CommandDispatcher;
import com.marketplace.repository.*;
import com.marketplace.service.MarketplaceService;
import com.marketplace.util.*;
import java.io.*;

/** Composition root and STDIN/STDOUT adapter. */
public final class Main {
    public static void main(String[] args) throws Exception {
        MarketplaceService service = new MarketplaceService(
            new InMemoryUserRepository(), new InMemoryListingRepository(),
            new InMemoryCategoryStatsRepository(), new IdGenerator(), new SystemTimeProvider());
        CommandDispatcher dispatcher = new CommandDispatcher(service);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            for (String output : dispatcher.execute(line)) System.out.println(output);
        }
    }
}
