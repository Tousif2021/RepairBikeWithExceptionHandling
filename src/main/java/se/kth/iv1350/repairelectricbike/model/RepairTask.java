package se.kth.iv1350.repairelectricbike.model;

import se.kth.iv1350.repairelectricbike.integration.RepairTaskDTO;

/**
 * A single task to be performed as part of a repair order, for example
 * "replace brake pads" with an associated cost.
 */
public class RepairTask {
    private final String description;
    private final Amount cost;

    /**
     * Creates a new {@link RepairTask} from the values in the given DTO.
     *
     * @param dto The DTO carrying task data from the view.
     */
    public RepairTask(RepairTaskDTO dto) {
        this.description = dto.getDescription();
        this.cost = dto.getCost();
    }

    /**
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The task cost.
     */
    public Amount getCost() {
        return cost;
    }
}
