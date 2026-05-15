package se.kth.iv1350.repairelectricbike.integration;

import java.util.ArrayList;
import java.util.List;
import se.kth.iv1350.repairelectricbike.exception.DatabaseFailureException;
import se.kth.iv1350.repairelectricbike.model.RepairOrderObserver;
import se.kth.iv1350.repairelectricbike.model.discount.DiscountStrategy;
import se.kth.iv1350.repairelectricbike.model.discount.LoyaltyDiscount;
import se.kth.iv1350.repairelectricbike.model.discount.NoDiscount;

/**
 * Handles access to stored repair orders. In a real application this
 * class would communicate with a database; here the orders are stored
 * as {@link RepairOrderDTO} snapshots in an in-memory list.
 *
 * <p>Only {@link RepairOrderDTO} objects (integration layer) are ever
 * stored here — no model objects cross the layer boundary into this
 * class.</p>
 *
 * <p>Looking up order ID {@value #DB_FAILURE_TRIGGER_ID} always throws
 * a {@link DatabaseFailureException}, simulating an unavailable
 * database server.</p>
 *
 * <p>Observers registered via {@link #addObserver(RepairOrderObserver)}
 * are stored here and handed to the {@link Controller} when a new
 * order is created, so the controller can attach them to the new
 * {@link se.kth.iv1350.repairelectricbike.model.RepairOrder}.</p>
 */
public class RepairOrderRegistry {

    /**
     * Order ID that always triggers a simulated database failure.
     */
    public static final int DB_FAILURE_TRIGGER_ID = 999;

    private static final int LOYALTY_DISCOUNT_EVERY_N = 3;

    private final List<RepairOrderDTO> repairOrders = new ArrayList<>();
    private final List<RepairOrderObserver> observers = new ArrayList<>();
    private int nextOrderId = 1;

    /**
     * Package-private constructor, called only by {@link RegistryCreator}.
     */
    RepairOrderRegistry() {
    }

    /**
     * Registers an observer to be notified whenever a repair order is
     * updated. Stored here so the controller can attach them to new
     * orders on creation.
     *
     * @param observer The observer to add.
     */
    public void addObserver(RepairOrderObserver observer) {
        observers.add(observer);
    }

    /**
     * Returns all registered observers. Called by the controller when
     * creating a new repair order, so it can attach each observer to
     * the new model object.
     *
     * @return An unmodifiable copy of the observer list.
     */
    public List<RepairOrderObserver> getObservers() {
        return new ArrayList<>(observers);
    }

    /**
     * Allocates a new unique order identifier and returns it. Called
     * by the controller before constructing a new
     * {@link se.kth.iv1350.repairelectricbike.model.RepairOrder}.
     *
     * @return The next available order ID.
     */
    public int nextOrderId() {
        return nextOrderId++;
    }

    /**
     * Determines the appropriate {@link DiscountStrategy} for a new
     * order belonging to the given customer, based on how many previous
     * orders that customer already has in the registry.
     *
     * @param customerPhoneNumber The phone number identifying the customer.
     * @return {@link LoyaltyDiscount} if this is the customer's third
     *         (or sixth, ninth, …) order; {@link NoDiscount} otherwise.
     */
    public DiscountStrategy discountFor(String customerPhoneNumber) {
        long previousOrders = repairOrders.stream()
                .filter(dto -> dto.getCustomer().getPhoneNumber()
                        .equals(customerPhoneNumber))
                .count();
        long orderNumber = previousOrders + 1; // this will be order #N
        return (orderNumber % LOYALTY_DISCOUNT_EVERY_N == 0)
                ? new LoyaltyDiscount()
                : new NoDiscount();
    }

    /**
     * Persists (saves or replaces) a repair order snapshot. If an
     * entry with the same order ID already exists it is replaced;
     * otherwise the snapshot is appended. Called by the controller
     * after every mutation to keep the stored snapshot up to date.
     *
     * @param snapshot An immutable DTO snapshot of the updated order.
     */
    public void save(RepairOrderDTO snapshot) {
        for (int i = 0; i < repairOrders.size(); i++) {
            if (repairOrders.get(i).getOrderId() == snapshot.getOrderId()) {
                repairOrders.set(i, snapshot);
                return;
            }
        }
        repairOrders.add(snapshot);
    }

    /**
     * Returns all repair orders currently stored in the registry.
     *
     * @return An array of DTOs, one per stored order. Empty if none exist.
     */
    public RepairOrderDTO[] findAllRepairOrders() {
        return repairOrders.toArray(new RepairOrderDTO[0]);
    }

    /**
     * Looks up the stored DTO for the specified order identifier.
     *
     * @param orderId The order identifier.
     * @return The matching {@link RepairOrderDTO}, or {@code null} if
     *         no such order exists.
     * @throws DatabaseFailureException if {@code orderId} equals
     *         {@value #DB_FAILURE_TRIGGER_ID}, simulating an
     *         unavailable database server.
     */
    public RepairOrderDTO findRepairOrderDTO(int orderId) {
        if (orderId == DB_FAILURE_TRIGGER_ID) {
            throw new DatabaseFailureException(
                    "Database server is not responding during repair order lookup.",
                    new RuntimeException("Simulated connection timeout"));
        }
        for (RepairOrderDTO dto : repairOrders) {
            if (dto.getOrderId() == orderId) {
                return dto;
            }
        }
        return null;
    }
}
