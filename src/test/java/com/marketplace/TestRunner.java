
package com.marketplace;
import com.marketplace.service.MarketplaceServiceTest;
import com.marketplace.repository.RepositoryConcurrencyTest;
import com.marketplace.cli.CommandParserTest;

import java.lang.reflect.Method;

public class TestRunner {

    public static void main(String[] args) throws Exception {

        runTests(new MarketplaceServiceTest());
        runTests(new RepositoryConcurrencyTest());
        runTests(new CommandParserTest());

        System.out.println("\nAll tests completed.");
    }

    private static void runTests(Object testClass) throws Exception {

        for (Method method : testClass.getClass().getDeclaredMethods()) {

            if (method.getName().startsWith("test")) {

                try {
                    method.invoke(testClass);

                    System.out.println(
                            "[PASS] " + method.getName()
                    );

                } catch (Exception e) {

                    System.out.println(
                            "[FAIL] " + method.getName()
                    );

                    Throwable cause = e.getCause();

                    if (cause != null) {
                        cause.printStackTrace();
                    } else {
                        e.printStackTrace();
                    }

                    throw new RuntimeException(
                            "Test failed: " + method.getName(), e
                    );
                }
            }
        }
    }
}
