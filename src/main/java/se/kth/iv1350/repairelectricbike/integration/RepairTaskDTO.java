package se.kth.iv1350.repairelectricbike.integration;

import se.kth.iv1350.repairelectricbike.model.Amount;

/**
 * Immutable data transfer object describing a single repair task that
 * is to be performed on a bike.
 */
public final class RepairTaskDTO {
    private final String description;
    private final Amount cost;

    /**
     * Creates a new {@link RepairTaskDTO}.
     *
     * @param description A short description of the task.
     * @param cost        The cost charged to the customer for this task.
     */
    public RepairTaskDTO(String description, Amount cost) {
        this.description = description;
        this.cost = cost;
    }

    /**
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The cost of this task.
     */
    public Amount getCost() {
        return cost;
    }
}
