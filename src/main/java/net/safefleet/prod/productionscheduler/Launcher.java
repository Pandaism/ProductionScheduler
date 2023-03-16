package net.safefleet.prod.productionscheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Launcher class for the Production Scheduler application. (Hack around to launch on Ubuntu)
 */
public class Launcher {
    // Create a Logger instance to log messages
    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    /**
     * The main entry point for the application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        // Log a message indicating the Production Scheduler has started successfully and display its version
        LOGGER.info("Production Schedule started successfully. Version: {}", Launcher.class.getPackage().getImplementationVersion());

        // Call the main method of the ProductionScheduler class
        ProductionScheduler.main(args);
    }
}
