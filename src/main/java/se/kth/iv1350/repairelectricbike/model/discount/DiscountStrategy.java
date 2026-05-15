package se.kth.iv1350.repairelectricbike.model.discount;

import se.kth.iv1350.repairelectricbike.model.Amount;

/**
 * Strategy interface for calculating discounts on a repair order.
 *
 * <p>Using the Strategy pattern here makes it possible to swap or
 * combine discount rules without changing {@link
 * se.kth.iv1350.repairelectricbike.model.RepairOrder}. New discount
 * types (e.g. seasonal, warranty-based) can be added by implementing
 * this interface without touching existing code.</p>
 */
public interface DiscountStrategy {

    /**
     * Calculates the discounted total for a given base cost.
     *
     * @param baseCost The undiscounted total cost of the repair order.
     * @return The cost after applying this discount.
     */
    Amount applyDiscount(Amount baseCost);
}
