package se.kth.iv1350.repairelectricbike.integration;

/**
 * Immutable data transfer object describing a customer. Used to pass
 * customer data between the integration, controller and view layers
 * without exposing the internal storage representation.
 */
public final class CustomerDTO {
    private final String name;
    private final String email;
    private final String phoneNumber;

    /**
     * Creates a new {@link CustomerDTO}.
     *
     * @param name        The customer's full name.
     * @param email       The customer's email address.
     * @param phoneNumber The customer's phone number.
     */
    public CustomerDTO(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return The customer's name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The customer's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return The customer's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
