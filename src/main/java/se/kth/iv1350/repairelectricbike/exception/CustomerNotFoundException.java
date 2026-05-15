package se.kth.iv1350.repairelectricbike.exception;

/**
 * Thrown when a search is made for a phone number that does not exist
 * in the customer registry. This corresponds to alternative flow 5a in
 * the Repair Electric Bike scenario.
 *
 * <p>This is a <em>checked</em> exception because it represents an
 * expected business error that the caller (the view) must explicitly
 * handle by informing the user.</p>
 */
public class CustomerNotFoundException extends Exception {

    private final String phoneNumber;

    /**
     * Creates a new {@link CustomerNotFoundException}.
     *
     * @param phoneNumber The phone number that was searched for but not
     *                    found in the customer registry.
     */
    public CustomerNotFoundException(String phoneNumber) {
        super("No customer found with phone number: " + phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return The phone number that was not found.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
