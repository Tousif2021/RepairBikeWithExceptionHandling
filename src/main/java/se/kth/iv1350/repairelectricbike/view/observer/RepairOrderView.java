package se.kth.iv1350.repairelectricbike.view.observer;

/**
 * An observer that displays updated repair order information to
 * technicians and receptionists on {@code System.out}.
 *
 * <p>Extends {@link RepairOrderObserverTemplate} so that error handling
 * is centralised in the template class. This class implements only the
 * display logic in {@link #doHandleRepairOrderUpdate()} and the error
 * fallback in {@link #handleErrors(Exception)}.</p>
 *
 * <p>This class never calls the controller or any other class; it only
 * receives data through the Observer interface.</p>
 */
public class RepairOrderView extends RepairOrderObserverTemplate {

    /**
     * Prints the contents of the updated repair order to
     * {@code System.out}.
     *
     * @throws Exception never in this implementation, but declared to
     *                   satisfy the template contract.
     */
    @Override
    protected void doHandleRepairOrderUpdate() throws Exception {
        System.out.println("\n[RepairOrderView] Repair order updated:");
        System.out.println("  Order ID  : " + updatedOrder.getOrderId());
        System.out.println("  State     : " + updatedOrder.getState());
        System.out.println("  Customer  : " + updatedOrder.getCustomer().getName());
        System.out.println("  Bike      : " + updatedOrder.getBike().getBrand()
                + " " + updatedOrder.getBike().getModel());
        System.out.println("  Description: " + updatedOrder.getDescription());
        System.out.println("  Total cost: " + updatedOrder.getTotalCost() + " SEK");
    }

    /**
     * Prints a warning to {@code System.err} if the view cannot render
     * the update.
     *
     * @param e The exception that prevented the update from being shown.
     */
    @Override
    protected void handleErrors(Exception e) {
        System.err.println("[RepairOrderView] Could not display update: "
                + e.getMessage());
    }
}
