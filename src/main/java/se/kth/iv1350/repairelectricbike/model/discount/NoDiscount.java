package se.kth.iv1350.repairelectricbike.model.discount;

import se.kth.iv1350.repairelectricbike.model.Amount;

/**
 * A {@link DiscountStrategy} that applies no discount. Used as the
 * default for customers who have not yet earned a loyalty discount.
 * This acts as a null-object, eliminating the need for null checks on
 * the strategy field in {@link
 * se.kth.iv1350.repairelectricbike.model.RepairOrder}.
 */
public class NoDiscount implements DiscountStrategy {

    /**
     * Returns the base cost unchanged.
     *
     * @param baseCost The undiscounted total cost.
     * @return The same cost, unmodified.
     */
    @Override
    public Amount applyDiscount(Amount baseCost) {
        return baseCost;
    }
}
