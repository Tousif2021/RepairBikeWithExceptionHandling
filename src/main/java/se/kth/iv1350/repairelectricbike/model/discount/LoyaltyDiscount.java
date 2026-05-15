package se.kth.iv1350.repairelectricbike.model.discount;

import se.kth.iv1350.repairelectricbike.model.Amount;

/**
 * A {@link DiscountStrategy} that grants a 10 percent loyalty discount.
 * Applied automatically on every third repair order for a returning
 * customer.
 */
public class LoyaltyDiscount implements DiscountStrategy {

    private static final double DISCOUNT_RATE = 0.10;

    /**
     * Returns the base cost reduced by {@value #DISCOUNT_RATE} (10 %).
     *
     * @param baseCost The undiscounted total cost.
     * @return The cost after the 10 % loyalty discount.
     */
    @Override
    public Amount applyDiscount(Amount baseCost) {
        double discounted = baseCost.getAmount() * (1.0 - DISCOUNT_RATE);
        return new Amount(discounted);
    }
}
