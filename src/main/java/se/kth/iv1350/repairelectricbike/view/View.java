package se.kth.iv1350.repairelectricbike.view;

import se.kth.iv1350.repairelectricbike.controller.Controller;
import se.kth.iv1350.repairelectricbike.exception.CustomerNotFoundException;
import se.kth.iv1350.repairelectricbike.exception.DatabaseFailureException;
import se.kth.iv1350.repairelectricbike.integration.BikeDTO;
import se.kth.iv1350.repairelectricbike.integration.CustomerDTO;
import se.kth.iv1350.repairelectricbike.integration.RepairOrderDTO;
import se.kth.iv1350.repairelectricbike.integration.RepairTaskDTO;
import se.kth.iv1350.repairelectricbike.model.Amount;
import se.kth.iv1350.repairelectricbike.view.observer.RepairOrderLogger;
import se.kth.iv1350.repairelectricbike.view.observer.RepairOrderView;

/**
 * Placeholder for the real view. Contains hard-coded calls to the
 * controller and prints everything the controller returns.
 *
 * <p>Registers two observers on startup: {@link RepairOrderView}
 * (prints updates to {@code System.out}) and {@link RepairOrderLogger}
 * (appends updates to {@code repair-order-log.txt}).</p>
 */
public class View {
    private final Controller contr;
    private final ErrorLogger errorLogger = new ErrorLogger();

    /**
     * Creates a new {@link View} connected to the given controller and
     * registers the observer implementations.
     *
     * @param contr The controller to call.
     */
    public View(Controller contr) {
        this.contr = contr;
        contr.addRepairOrderObserver(new RepairOrderView());
        contr.addRepairOrderObserver(new RepairOrderLogger());
    }

    /**
     * Runs a full execution demonstrating the happy path, exception
     * handling (customer not found, database failure), the Observer
     * pattern, the Strategy (loyalty discount), and the Singleton.
     */
    public void execution() {
        demonstrateHappyPath();
        demonstrateCustomerNotFound();
        demonstrateDatabaseFailure();
        demonstrateLoyaltyDiscount();
    }

    // ---------------------------------------------------------------
    // Happy-path scenario
    // ---------------------------------------------------------------
    private void demonstrateHappyPath() {
        System.out.println("\n====== HAPPY PATH ======");

        System.out.println("\n>> Receptionist looks up customer by phone 070-1234567");
        CustomerDTO customer = findCustomerSafely("070-1234567");
        if (customer == null) return;
        System.out.println("Found: " + formatCustomer(customer));

        System.out.println("\n>> Receptionist registers a new repair order");
        BikeDTO bike = new BikeDTO("Kawasaki", "Pro Mountain Bike", "0012");
        RepairOrderDTO order = contr.createRepairOrder(
                "Battery will not charge", customer, bike);
        System.out.println("Created: " + formatOrder(order));

        System.out.println("\n>> Technician records diagnostic findings");
        contr.addDiagnosticResult(order.getOrderId(), "Battery cells are dead");
        contr.addDiagnosticResult(order.getOrderId(), "Charger port is loose");

        System.out.println("\n>> Technician adds repair tasks");
        contr.addRepairTask(order.getOrderId(),
                new RepairTaskDTO("Replace battery pack", new Amount(3500.00)));
        contr.addRepairTask(order.getOrderId(),
                new RepairTaskDTO("Fix charger port", new Amount(450.00)));

        System.out.println("\n>> Technician finalizes the order");
        contr.updateRepairOrder(order.getOrderId(),
                "Replace battery pack and repair loose charger port");

        System.out.println("\n>> Customer accepts the repair order");
        contr.acceptRepairOrder(order.getOrderId());

        System.out.println("\n>> Final printout");
        contr.printRepairOrder(order.getOrderId());
    }

    // ---------------------------------------------------------------
    // Alternative flow 5a — customer not found (checked exception)
    // ---------------------------------------------------------------
    private void demonstrateCustomerNotFound() {
        System.out.println("\n====== ALT FLOW 5a: CUSTOMER NOT FOUND ======");
        System.out.println(">> Searching for non-existent phone number 000-0000000");
        findCustomerSafely("000-0000000");
    }

    // ---------------------------------------------------------------
    // Database failure (unchecked exception)
    // ---------------------------------------------------------------
    private void demonstrateDatabaseFailure() {
        System.out.println("\n====== DATABASE FAILURE SIMULATION ======");
        System.out.println(">> Searching with DB-failure trigger phone 000-DATABASE-FAIL");
        findCustomerSafely("000-DATABASE-FAIL");
    }

    // ---------------------------------------------------------------
    // Loyalty discount (Strategy pattern) — every 3rd order
    // ---------------------------------------------------------------
    private void demonstrateLoyaltyDiscount() {
        System.out.println("\n====== LOYALTY DISCOUNT (every 3rd order) ======");
        CustomerDTO customer = findCustomerSafely("072-1234567");
        if (customer == null) return;
        BikeDTO bike = new BikeDTO("Trek", "FX3", "TRK-001");
        for (int i = 1; i <= 3; i++) {
            RepairOrderDTO o = contr.createRepairOrder("Service #" + i, customer, bike);
            contr.addRepairTask(o.getOrderId(),
                    new RepairTaskDTO("Standard service", new Amount(1000.00)));
            RepairOrderDTO updated = contr.findAllRepairOrders()[
                    contr.findAllRepairOrders().length - 1];
            System.out.println("Order " + i + " total: " + updated.getTotalCost() + " SEK"
                    + (i == 3 ? "  <-- 10% loyalty discount applied" : ""));
        }
    }

    // ---------------------------------------------------------------
    // Helper: wraps findCustomer to handle both exception types
    // ---------------------------------------------------------------
    private CustomerDTO findCustomerSafely(String phoneNumber) {
        try {
            return contr.findCustomer(phoneNumber);
        } catch (CustomerNotFoundException ex) {
            System.out.println("ERROR: " + ex.getMessage()
                    + " — please check the phone number and try again.");
            // CustomerNotFoundException is an expected business error;
            // no need to log it as a developer error.
            return null;
        } catch (DatabaseFailureException ex) {
            System.out.println("ERROR: The system is temporarily unavailable. "
                    + "Please try again later.");
            errorLogger.log(ex);
            return null;
        }
    }

    private String formatCustomer(CustomerDTO c) {
        if (c == null) return "null";
        return c.getName() + " <" + c.getEmail() + "> " + c.getPhoneNumber();
    }

    private String formatOrder(RepairOrderDTO o) {
        if (o == null) return "null";
        return "Order #" + o.getOrderId() + " [" + o.getState() + "] "
                + o.getDescription() + " (total: " + o.getTotalCost() + " SEK)";
    }
}
