package se.kth.iv1350.repairelectricbike.view.observer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import se.kth.iv1350.repairelectricbike.integration.RepairOrderDTO;
import se.kth.iv1350.repairelectricbike.model.RepairOrderObserver;

/**
 * An observer that logs every repair order update to a file called
 * {@code repair-order-log.txt}. Useful as an audit trail for all
 * changes made to repair orders during a session.
 *
 * <p>This class never calls the controller or any other class; it only
 * receives data through the Observer interface.</p>
 */
public class RepairOrderLogger implements RepairOrderObserver {

    private static final String LOG_FILE = "repair-order-log.txt";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private PrintWriter logWriter;

    /**
     * Creates a new {@link RepairOrderLogger}. Opens the log file in
     * append mode. Prints a warning to {@code System.err} if the file
     * cannot be opened.
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
     * @param updatedOrder An immutable snapshot of the updated order.
     */
    @Override
    public void repairOrderUpdated(RepairOrderDTO updatedOrder) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        if (logWriter != null) {
            logWriter.println("[" + timestamp + "] Order #" + updatedOrder.getOrderId()
                    + " | State: " + updatedOrder.getState()
                    + " | Customer: " + updatedOrder.getCustomer().getName()
                    + " | Description: " + updatedOrder.getDescription()
                    + " | Total: " + updatedOrder.getTotalCost() + " SEK");
        }
    }
}
