package se.kth.iv1350.repairelectricbike.model;

/**
 * Represents an amount of money. Instances are immutable.
 */
public final class Amount {
    private final double amount;

    /**
     * Creates a new {@link Amount} with the specified value.
     *
     * @param amount The amount of money.
     */
    public Amount(double amount) {
        this.amount = amount;
    }

    /**
     * @return The numeric value of this amount.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns a new {@link Amount} that is the sum of this amount and
     * the specified other amount.
     *
     * @param other The amount to add.
     * @return The sum.
     */
    public Amount plus(Amount other) {
        return new Amount(this.amount + other.amount);
    }

    /**
     * Two amounts are equal when they hold the same numeric value.
     *
     * @param obj The object to compare with.
     * @return {@code true} if equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Amount)) {
            return false;
        }
        return Double.compare(((Amount) obj).amount, this.amount) == 0;
    }

    /**
     * @return A hash code consistent with {@link #equals(Object)}.
     */
    @Override
    public int hashCode() {
        return Double.hashCode(amount);
    }

    /**
     * @return A string representation of this amount with two decimals.
     */
    @Override
    public String toString() {
        return String.format("%.2f", amount);
    }
}
