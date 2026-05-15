package se.kth.iv1350.repairelectricbike.view;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Writes error reports to a log file on disk. One instance is created
 * per application run; the file is opened in append mode so that log
 * entries accumulate across runs.
 */
class ErrorLogger {

    private static final String LOG_FILE = "error-log.txt";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private PrintWriter logWriter;

    /**
     * Creates a new {@link ErrorLogger}. Opens the log file in append
     * mode. If the file cannot be opened, a message is printed to
     * {@code System.err} instead.
     */
    ErrorLogger() {
        try {
            logWriter = new PrintWriter(new FileWriter(LOG_FILE, true), true);
        } catch (IOException ex) {
            System.err.println("WARNING: Could not open log file " + LOG_FILE
                    + ". Error details will be printed to stderr.");
            ex.printStackTrace();
        }
    }

    /**
     * Logs an exception with a timestamp and full stack trace.
     *
     * @param ex The exception to log.
     */
    void log(Exception ex) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        if (logWriter != null) {
            logWriter.println("[" + timestamp + "] " + ex.getClass().getName()
                    + ": " + ex.getMessage());
            ex.printStackTrace(logWriter);
            logWriter.println();
        } else {
            System.err.println("[" + timestamp + "] " + ex.getClass().getName()
                    + ": " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }
}
