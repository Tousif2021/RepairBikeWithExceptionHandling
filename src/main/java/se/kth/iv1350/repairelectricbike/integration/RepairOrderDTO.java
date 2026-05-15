package se.kth.iv1350.repairelectricbike.integration;

import se.kth.iv1350.repairelectricbike.model.Amount;

/**
 * Immutable data transfer object describing a repair order. Sent from
 * the controller to the view so the view can display the order without
 * being coupled to the model's {@code RepairOrder} class.
 */
public final class RepairOrderDTO {
    private final int orderId;
    private final String description;
    private final String state;
    private final CustomerDTO customer;
    private final BikeDTO bike;
    private final Amount totalCost;

    /**
     * Creates a new {@link RepairOrderDTO}.
     *
     * @param orderId     The order identifier.
     * @param description Short description of the reported problem.
     * @param state       The order's current state as a string.
     * @param customer    The customer owning the bike.
     * @param bike        The bike being repaired.
     * @param totalCost   The sum of all repair task costs.
     */
    public RepairOrderDTO(int orderId, String description, String state,
                          CustomerDTO customer, BikeDTO bike, Amount totalCost) {
        this.orderId = orderId;
        this.description = description;
        this.state = state;
        this.customer = customer;
        this.bike = bike;
        this.totalCost = totalCost;
    }

    /**
     * @return The order identifier.
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * @return The description of the reported problem.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The order state.
     */
    public String getState() {
        return state;
    }

    /**
     * @return The customer.
     */
    public CustomerDTO getCustomer() {
        return customer;
    }

    /**
     * @return The bike.
     */
    public BikeDTO getBike() {
        return bike;
    }

    /**
     * @return The total cost of all repair tasks in this order.
     */
    public Amount getTotalCost() {
        return totalCost;
    }
}
