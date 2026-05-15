package se.kth.iv1350.repairelectricbike.model.discount;

import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairelectricbike.model.Amount;

import static org.junit.jupiter.api.Assertions.*;

class DiscountStrategyTest {

    @Test
    void noDiscountReturnsExactBaseCost() {
        DiscountStrategy strategy = new NoDiscount();
        Amount base = new Amount(1000.0);
        Amount result = strategy.applyDiscount(base);
        assertEquals(base, result,
                "NoDiscount should return the base cost unchanged");
    }

    @Test
    void loyaltyDiscountReducesCostByTenPercent() {
        DiscountStrategy strategy = new LoyaltyDiscount();
        Amount base = new Amount(1000.0);
        Amount result = strategy.applyDiscount(base);
        assertEquals(900.0, result.getAmount(), 0.001,
                "LoyaltyDiscount should reduce cost by 10%");
    }

    @Test
    void loyaltyDiscountOnZeroCostIsZero() {
        DiscountStrategy strategy = new LoyaltyDiscount();
        Amount result = strategy.applyDiscount(new Amount(0.0));
        assertEquals(0.0, result.getAmount(), 0.001,
                "10% of zero should still be zero");
    }

    @Test
    void noDiscountOnZeroCostIsZero() {
        DiscountStrategy strategy = new NoDiscount();
        Amount result = strategy.applyDiscount(new Amount(0.0));
        assertEquals(0.0, result.getAmount(), 0.001,
                "NoDiscount on zero cost should still be zero");
    }
}
