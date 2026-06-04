package se.kth.iv1350.repairelectricbike.view.observer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * An observer that logs every repair order update to a file called
 * {@code repair-order-log.txt}. Useful as an audit trail for all
 * changes made to repair orders during a session.
 *
 * <p>Extends {@link RepairOrderObserverTemplate} so that error handling
 * is centralised in the template class. If the log file cannot be
 * written, {@link #handleErrors(Exception)} falls back to
 * {@code System.err}.</p>
 *
 * <p>This class never calls the controller or any other class; it only
 * receives data through the Observer interface.</p>
 */
public class RepairOrderLogger extends RepairOrderObserverTemplate {

    private static final String LOG_FILE = "repair-order-log.txt";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private PrintWriter logWriter;

    /**
     * Creates a new {@link RepairOrderLogger}. Opens the log file in
     * append mode. If the file cannot be opened, a warning is printed
     * to {@code System.err} and every subsequent update will be handled
     * by {@link #handleErrors(Exception)}.
     */
    public RepairOrderLogger() {
        try {
            logWriter = new PrintWriter(new FileWriter(LOG_FILE, true), true);
        } catch (IOException ex) {
            System.err.println("WARNING: Could not open repair order log file "
                    + LOG_FILE + ": " + ex.getMessage());
        }
    }

    /**
     * Appends a timestamped entry for the updated repair order to the
     * log file.
     *
     * @throws IOException if the log file is not open or cannot be written.
     */
    @Override
    protected void doHandleRepairOrderUpdate() throws Exception {
        if (logWriter == null) {
            throw new IOException("Log file is not open.");
        }
        String timestamp = LocalDateTime.now().format(FORMATTER);
        logWriter.println("[" + timestamp + "] Order #" + updatedOrder.getOrderId()
                + " | State: " + updatedOrder.getState()
                + " | Customer: " + updatedOrder.getCustomer().getName()
                + " | Description: " + updatedOrder.getDescription()
                + " | Total: " + updatedOrder.getTotalCost() + " SEK");
    }

    /**
     * Falls back to {@code System.err} if the log file cannot be written.
     *
     * @param e The exception thrown by {@link #doHandleRepairOrderUpdate()}.
     */
    @Override
    protected void handleErrors(Exception e) {
        System.err.println("[RepairOrderLogger] Logging failed: " + e.getMessage());
    }
}
