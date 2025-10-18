package com.errol.db2spring.cli;

import com.errol.db2spring.logger.ColoredLogger;

import java.time.Duration;
import java.time.Instant;

public class CliLogger {

    public static void logInfo(String message) {
        ColoredLogger.info(message)
                .color(ColoredLogger.BRIGHT_WHITE)
                .withoutDashes()
                .log();
    }

    public static void logErrorAndExit(String message) {
        ColoredLogger.error(message)
                .color(ColoredLogger.RED)
                .withoutDashes()
                .log();
        System.exit(1);
    }

    public static void logHeader() {
        ColoredLogger.info("-")
                .withDashes()
                .log();

        ColoredLogger.info("Starting db2spring generator")
                .color(ColoredLogger.BRIGHT_GREEN)
                .align(ColoredLogger.Alignment.CENTER)
                .log();

        ColoredLogger.info("-")
                .withDashes()
                .log();
    }

    public static void logFooter(Instant start) {
        Instant end = Instant.now();
        double seconds = Duration.between(start, end).toMillis() / 1000.0;

        ColoredLogger.info("-")
                .withDashes()
                .log();

        ColoredLogger.info("db2spring generation completed successfully!")
                .color(ColoredLogger.BRIGHT_GREEN)
                .align(ColoredLogger.Alignment.CENTER)
                .withDashes()
                .log();

        ColoredLogger.info(String.format("Completed in %.2fs", seconds))
                .color(ColoredLogger.BRIGHT_GREEN)
                .align(ColoredLogger.Alignment.CENTER)
                .withDashes()
                .log();

        ColoredLogger.info("-")
                .withDashes()
                .log();
        // Exit with 0 for success
        System.exit(0); 
    }
}