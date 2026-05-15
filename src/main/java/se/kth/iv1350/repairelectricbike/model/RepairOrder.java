package se.kth.iv1350.repairelectricbike.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import se.kth.iv1350.repairelectricbike.integration.BikeDTO;
import se.kth.iv1350.repairelectricbike.integration.CustomerDTO;
import se.kth.iv1350.repairelectricbike.integration.Printer;
import se.kth.iv1350.repairelectricbike.integration.RepairOrderDTO;
import se.kth.iv1350.repairelectricbike.integration.RepairTaskDTO;
import se.kth.iv1350.repairelectricbike.model.discount.DiscountStrategy;
import se.kth.iv1350.repairelectricbike.model.discount.NoDiscount;

/**
 * Represents one particular repair order for one particular bike.
 *
 * <p>This class belongs to the model layer and holds no reference to
 * any integration-layer class. Persistence is handled externally by
 * the controller, which calls
 * {@link se.kth.iv1350.repairelectricbike.integration.RepairOrderRegistry#save(RepairOrderDTO)}
 * after each mutation.</p>
 *
 * <p>Implements the <em>observed</em> role in the Observer pattern.
 * Any number of {@link RepairOrderObserver} instances can be
 * registered and will be notified with a {@link RepairOrderDTO}
 * snapshot after every state change.</p>
 *
 * <p>Holds a {@link DiscountStrategy} that is applied when computing
 * the total cost, enabling flexible discount rules without modifying
 * this class.</p>
 */
public class RepairOrder {
    private final int orderId;
    private String description;
    private final CustomerDTO customer;
    private final BikeDTO bike;
    private final DiagnosticReport diagnosticReport = new DiagnosticReport();
    private final List<RepairTask> repairTasks = new ArrayList<>();
    private OrderState state;
    private final List<RepairOrderObserver> observers = new ArrayList<>();
    private DiscountStrategy discountStrategy = new NoDiscount();

    /**
     * Creates a new repair order in state {@link OrderState#CREATED}.
     *
     * @param orderId     Unique order identifier.
     * @param description Short description of the reported problem.
     * @param customer    The owner of the bike.
     * @param bike        The bike being brought in.
     */
    public RepairOrder(int orderId, String description,
                       CustomerDTO customer, BikeDTO bike) {
        this.orderId = orderId;
        this.description = description;
        this.customer = customer;
        this.bike = bike;
        this.state = OrderState.CREATED;
    }

    /**
     * Registers an observer that will be notified on every update to
     * this repair order.
     *
     * @param observer The observer to add.
     */
    public void addObserver(RepairOrderObserver observer) {
        observers.add(observer);
    }

    /**
     * Sets the discount strategy to apply when computing the total
     * cost of this order.
     *
     * @param strategy The strategy to use. Use
     *                 {@link se.kth.iv1350.repairelectricbike.model.discount.NoDiscount}
     *                 to apply no discount.
     */
    public void setDiscountStrategy(DiscountStrategy strategy) {
        this.discountStrategy = strategy;
    }

    /**
     * Records a diagnostic finding and notifies observers.
     *
     * @param finding The diagnostic finding to record.
     */
    public void addDiagnosticResult(String finding) {
        diagnosticReport.addFinding(finding);
        notifyObservers();
    }

    /**
     * Adds a repair task and notifies observers.
     *
     * @param taskDTO The task to add.
     */
    public void addRepairTask(RepairTaskDTO taskDTO) {
        repairTasks.add(new RepairTask(taskDTO));
        notifyObservers();
    }

    /**
     * Updates the description of this order, marks it as ready for
     * customer approval, and notifies observers.
     *
     * @param newDescription Updated description of the work to be done.
     */
    public void update(String newDescription) {
        this.description = newDescription;
        this.state = OrderState.READY_FOR_APPROVAL;
        notifyObservers();
    }

    /**
     * Sets the state of this order and notifies observers.
     *
     * @param newState The new state.
     */
    public void setState(OrderState newState) {
        this.state = newState;
        notifyObservers();
    }

    /**
     * Prints this repair order using the provided printer.
     *
     * @param printer The printer to use.
     */
    public void printRepairOrder(Printer printer) {
        printer.printRepairOrder(this);
    }

    /**
     * Returns the sum of the costs of all repair tasks, after applying
     * the current {@link DiscountStrategy}.
     *
     * @return The discounted total cost.
     */
    public Amount getTotalCost() {
        Amount base = new Amount(0);
        for (RepairTask task : repairTasks) {
            base = base.plus(task.getCost());
        }
        return discountStrategy.applyDiscount(base);
    }

    /**
     * Creates an immutable snapshot of this order's current state for
     * transport across layer boundaries.
     *
     * @return A {@link RepairOrderDTO} representing this order.
     */
    public RepairOrderDTO toDTO() {
        return new RepairOrderDTO(orderId, description, state.name(),
                customer, bike, getTotalCost());
    }

    /** @return The order identifier. */
    public int getOrderId() { return orderId; }

    /** @return The current description. */
    public String getDescription() { return description; }

    /** @return The customer. */
    public CustomerDTO getCustomer() { return customer; }

    /** @return The bike. */
    public BikeDTO getBike() { return bike; }

    /** @return The current state. */
    public OrderState getState() { return state; }

    /** @return The diagnostic report. */
    public DiagnosticReport getDiagnosticReport() { return diagnosticReport; }

    /** @return An unmodifiable view of the repair tasks. */
    public List<RepairTask> getRepairTasks() {
        return Collections.unmodifiableList(repairTasks);
    }

    private void notifyObservers() {
        RepairOrderDTO snapshot = toDTO();
        for (RepairOrderObserver observer : observers) {
            observer.repairOrderUpdated(snapshot);
        }
    }
}
