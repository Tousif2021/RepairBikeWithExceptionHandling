package se.kth.iv1350.repairelectricbike.view.observer;

import se.kth.iv1350.repairelectricbike.integration.RepairOrderDTO;
import se.kth.iv1350.repairelectricbike.model.RepairOrderObserver;

/**
 * Abstract template class for repair order observers. Defines the
 * invariant skeleton of the notification algorithm using the
 * <em>Template Method</em> design pattern.
 *
 * <p>The public entry point {@link #repairOrderUpdated(RepairOrderDTO)}
 * is declared {@code final} so that subclasses cannot bypass the
 * template structure. All error handling is centralised here: if
 * {@link #doHandleRepairOrderUpdate()} throws any exception, the call
 * is forwarded to {@link #handleErrors(Exception)} rather than
 * propagating up the call stack.</p>
 *
 * <p>Subclasses implement only the two abstract methods, which contain
 * the observer-specific logic. They access the current DTO via the
 * {@code protected} field {@link #updatedOrder}.</p>
 */
public abstract class RepairOrderObserverTemplate implements RepairOrderObserver {

    /**
     * The DTO of the most recently updated repair order. Set by the
     * template before {@link #doHandleRepairOrderUpdate()} is called,
     * so subclasses can access it without needing a method parameter.
     */
    protected RepairOrderDTO updatedOrder;

    /**
     * Entry point required by the {@link RepairOrderObserver} interface.
     * Stores the incoming DTO and delegates to the template method.
     * Declared {@code final} to prevent subclasses from bypassing the
     * try-catch error-handling structure.
     *
     * @param repairOrder An immutable snapshot of the updated order.
     */
    @Override
    public final void repairOrderUpdated(RepairOrderDTO repairOrder) {
        this.updatedOrder = repairOrder;
        handleRepairOrderUpdate();
    }

    private void handleRepairOrderUpdate() {
        try {
            doHandleRepairOrderUpdate();
        } catch (Exception e) {
            handleErrors(e);
        }
    }

    /**
     * Subclasses implement this method to perform the observer-specific
     * update logic. Any exception thrown here is caught by the template
     * and forwarded to {@link #handleErrors(Exception)}.
     *
     * @throws Exception if any error occurs during the update.
     */
    protected abstract void doHandleRepairOrderUpdate() throws Exception;

    /**
     * Subclasses implement this method to handle any exception thrown
     * by {@link #doHandleRepairOrderUpdate()}.
     *
     * @param e The exception that was thrown.
     */
    protected abstract void handleErrors(Exception e);
}
