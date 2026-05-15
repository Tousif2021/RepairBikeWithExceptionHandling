package se.kth.iv1350.repairelectricbike.model;

/**
 * The possible states of a {@link RepairOrder} during its lifecycle.
 */
public enum OrderState {
    /** The order has been created but no work has started. */
    CREATED,
    /** The technician has added diagnostics and tasks; awaiting customer approval. */
    READY_FOR_APPROVAL,
    /** The customer approved the quoted work. */
    ACCEPTED,
    /** The customer declined the quoted work. */
    REJECTED
}
