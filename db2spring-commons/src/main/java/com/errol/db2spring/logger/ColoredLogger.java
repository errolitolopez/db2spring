package com.errol.db2spring.logger;

public class ColoredLogger {

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BRIGHT_WHITE = "\u001B[97m";
    public static final String BRIGHT_GREEN = "\u001B[92m";

    private static final int LINE_LENGTH = 100;

    public enum Alignment {LEFT, CENTER, RIGHT}

    public enum Level {INFO, WARNING, ERROR}

    // -------------------- Chainable Logger --------------------
    public static LoggerBuilder info(String message) {
        return new LoggerBuilder(Level.INFO, message);
    }

    public static LoggerBuilder warning(String message) {
        return new LoggerBuilder(Level.WARNING, message);
    }

    public static LoggerBuilder error(String message) {
        return new LoggerBuilder(Level.ERROR, message);
    }

    public static class LoggerBuilder {
        private final LogOptions options;

        public LoggerBuilder(Level level, String message) {
            options = new LogOptions(message).level(level);
        }

        public LoggerBuilder color(String color) {
            options.messageColor(color);
            return this;
        }

        public LoggerBuilder bracketColor(String color) {
            options.bracketColor(color);
            return this;
        }

        public LoggerBuilder withDashes() {
            options.withDashes(true);
            return this;
        }

        public LoggerBuilder withoutDashes() {
            options.withDashes(false);
            return this;
        }

        public LoggerBuilder align(Alignment alignment) {
            options.alignment(alignment);
            return this;
        }

        public void log() {
            ColoredLogger.log(options);
        }
    }

    public static class LogOptions {
        private String message = "";
        private Level level = Level.INFO;
        private Alignment alignment = Alignment.LEFT;
        private boolean withDashes = true;
        private String bracketColor = RESET;
        private String levelColor = BLUE;
        private String messageColor = "";

        public LogOptions() {
        }

        public LogOptions(String message) {
            this.message = message;
        }

        public LogOptions level(Level level) {
            this.level = level;
            this.levelColor = switch (level) {
                case INFO -> BLUE;
                case WARNING -> YELLOW;
                case ERROR -> RED;
            };
            return this;
        }

        public LogOptions alignment(Alignment alignment) {
            this.alignment = alignment;
            return this;
        }

        public LogOptions withDashes(boolean withDashes) {
            this.withDashes = withDashes;
            return this;
        }

        public LogOptions bracketColor(String color) {
            this.bracketColor = color;
            return this;
        }

        public LogOptions messageColor(String color) {
            this.messageColor = color;
            return this;
        }
    }

    // -------------------- Core log logic --------------------
    public static void log(LogOptions options) {
        String coloredTag = options.bracketColor + "[" + options.levelColor + options.level + options.bracketColor + "]" + RESET;
        String messageColor = options.messageColor.isEmpty() ? "" : options.messageColor;
        int tagLength = 2 + options.level.name().length() + 1;
        int maxMessageLength = LINE_LENGTH - tagLength;

        String[] lines = options.message.split("\n");
        for (String line : lines) {
            logWrappedLine(coloredTag, line, options, messageColor, maxMessageLength, tagLength);
        }
    }

    private static void logWrappedLine(String coloredTag, String message, LogOptions options, String messageColor, int maxMessageLength, int tagLength) {
        // Handle completely empty line
        if (message.isEmpty()) {
            printLine(coloredTag, "", options, messageColor);
            return;
        }

        String[] words = message.split(" ");
        StringBuilder lineBuilder = new StringBuilder();

        for (String word : words) {
            while (word.length() > maxMessageLength) {
                if (!lineBuilder.isEmpty()) {
                    printLine(coloredTag, lineBuilder.toString(), options, messageColor);
                    lineBuilder.setLength(0);
                }
                printLine(coloredTag, word.substring(0, maxMessageLength), options, messageColor);
                word = word.substring(maxMessageLength);
            }

            if (lineBuilder.length() + word.length() + (!lineBuilder.isEmpty() ? 1 : 0) > maxMessageLength) {
                printLine(coloredTag, lineBuilder.toString(), options, messageColor);
                lineBuilder.setLength(0);
            }

            if (!lineBuilder.isEmpty()) lineBuilder.append(" ");
            lineBuilder.append(word);
        }

        // Print remaining content or empty line
        printLine(coloredTag, lineBuilder.toString(), options, messageColor);
    }

    private static void printLine(String coloredTag, String line, LogOptions options, String messageColor) {
        String coloredLine = (messageColor.isEmpty() ? "" : messageColor) + line + RESET;
        int remainingLength = LINE_LENGTH - visibleLength(coloredTag) - visibleLength(line);

        int leftDashes = 0, rightDashes = 0;
        if (options.withDashes) {
            switch (options.alignment) {
                case LEFT -> rightDashes = remainingLength;
                case CENTER -> {
                    leftDashes = remainingLength / 2;
                    rightDashes = remainingLength - leftDashes;
                }
                case RIGHT -> leftDashes = remainingLength;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(coloredTag).append(" ");
        if (options.withDashes) sb.append("-".repeat(Math.max(0, leftDashes)));
        sb.append(coloredLine);
        if (options.withDashes) sb.append("-".repeat(Math.max(0, rightDashes)));

        System.out.println(sb);
    }

    private static int visibleLength(String str) {
        return str.replaceAll("\u001B\\[[;\\d]*m", "").length();
    }

    // -------------------- Test --------------------
    public static void main(String[] args) {
        ColoredLogger.info("Hello World").color(RED).withDashes().log();
        ColoredLogger.warning("Warning!").color(YELLOW).align(Alignment.CENTER).log();
        ColoredLogger.error("Something went wrong!").color(GREEN).withoutDashes().log();
    }
}
