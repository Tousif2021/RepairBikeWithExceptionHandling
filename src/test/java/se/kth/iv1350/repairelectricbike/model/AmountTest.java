package se.kth.iv1350.repairelectricbike.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link Amount} class. Verifies that the value is stored
 * correctly, that addition works, and that equality is based on value.
 */
public class AmountTest {

    @Test
    public void getAmountReturnsValuePassedToConstructor() {
        Amount instance = new Amount(123.45);
        assertEquals(123.45, instance.getAmount(), 0.0001,
                "Amount.getAmount should return the value passed to the constructor.");
    }

    @Test
    public void plusReturnsSumOfBothValues() {
        Amount a = new Amount(100.00);
        Amount b = new Amount(50.25);
        Amount result = a.plus(b);
        assertEquals(150.25, result.getAmount(), 0.0001,
                "plus() should return the sum of the two amounts.");
    }

    @Test
    public void plusDoesNotMutateOperands() {
        Amount a = new Amount(100.00);
        Amount b = new Amount(50.00);
        a.plus(b);
        assertEquals(100.00, a.getAmount(), 0.0001, "Amount must be immutable.");
        assertEquals(50.00, b.getAmount(), 0.0001, "Amount must be immutable.");
    }

    @Test
    public void plusReturnsNewInstance() {
        Amount a = new Amount(10.0);
        Amount b = new Amount(5.0);
        Amount result = a.plus(b);
        assertNotSame(a, result, "plus() must return a new instance.");
        assertNotSame(b, result, "plus() must return a new instance.");
    }

    @Test
    public void equalsReturnsTrueForEqualValues() {
        Amount a = new Amount(99.99);
        Amount b = new Amount(99.99);
        assertTrue(a.equals(b), "Amounts with the same value should be equal.");
    }

    @Test
    public void equalsReturnsFalseForDifferentValues() {
        Amount a = new Amount(99.99);
        Amount b = new Amount(100.00);
        assertFalse(a.equals(b), "Amounts with different values should not be equal.");
    }

    @Test
    public void equalsReturnsFalseForNonAmountObject() {
        Amount a = new Amount(10.0);
        assertFalse(a.equals("10.0"), "Amount should not equal a non-Amount object.");
        assertFalse(a.equals(null), "Amount should not equal null.");
    }

    @Test
    public void hashCodeIsEqualForEqualAmounts() {
        Amount a = new Amount(42.0);
        Amount b = new Amount(42.0);
        assertEquals(a.hashCode(), b.hashCode(),
                "Equal amounts must have equal hash codes.");
    }

    @Test
    public void zeroAmountIsHandledCorrectly() {
        Amount zero = new Amount(0);
        Amount other = new Amount(100);
        assertEquals(100.0, zero.plus(other).getAmount(), 0.0001,
                "Adding zero should yield the other amount.");
    }
}
