package se.kth.iv1350.repairelectricbike.view.observer;

import se.kth.iv1350.repairelectricbike.integration.RepairOrderDTO;
import se.kth.iv1350.repairelectricbike.model.RepairOrderObserver;

/**
 * An observer that displays updated repair order information to
 * technicians and receptionists on {@code System.out}.
 *
 * <p>This implementation replaces the manual "ask system for repair
 * order" step (bullet 13) in the Repair Electric Bike scenario, and
 * solves the problem of how the receptionist is informed of updated
 * orders (bullet 18). It is notified automatically by the model
 * whenever a {@link se.kth.iv1350.repairelectricbike.model.RepairOrder}
 * changes.</p>
 *
 * <p>This class never calls the controller or any other class; it only
 * receives data through the Observer interface.</p>
 */
public class RepairOrderView implements RepairOrderObserver {

    /**
     * Prints the contents of the updated repair order to
     * {@code System.out}.
     *
     * @param updatedOrder An immutable snapshot of the updated order.
     */
    @Override
    public void repairOrderUpdated(RepairOrderDTO updatedOrder) {
        System.out.println("\n[RepairOrderView] Repair order updated:");
        System.out.println("  Order ID  : " + updatedOrder.getOrderId());
        System.out.println("  State     : " + updatedOrder.getState());
        System.out.println("  Customer  : " + updatedOrder.getCustomer().getName());
        System.out.println("  Bike      : " + updatedOrder.getBike().getBrand()
                + " " + updatedOrder.getBike().getModel());
        System.out.println("  Description: " + updatedOrder.getDescription());
        System.out.println("  Total cost: " + updatedOrder.getTotalCost() + " SEK");
    }
}
