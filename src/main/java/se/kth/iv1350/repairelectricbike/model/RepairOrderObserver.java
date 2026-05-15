package se.kth.iv1350.repairelectricbike.model;

import se.kth.iv1350.repairelectricbike.integration.RepairOrderDTO;

/**
 * Implemented by classes that want to be notified whenever a
 * {@link RepairOrder} is updated.
 *
 * <p>Using this interface as part of the Observer pattern decouples
 * the model ({@link RepairOrder}) from any concrete view or logger
 * class. The model only knows about this interface, not about the
 * concrete implementations.</p>
 */
public interface RepairOrderObserver {

    /**
     * Called by {@link RepairOrder} whenever the order has been
     * updated in any way.
     *
     * @param updatedOrder An immutable snapshot of the repair order's
     *                     state at the time of the update.
     */
    void repairOrderUpdated(RepairOrderDTO updatedOrder);
}
