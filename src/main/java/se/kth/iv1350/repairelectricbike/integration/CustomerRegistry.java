package se.kth.iv1350.repairelectricbike.integration;

import java.util.ArrayList;
import java.util.List;
import se.kth.iv1350.repairelectricbike.exception.CustomerNotFoundException;
import se.kth.iv1350.repairelectricbike.exception.DatabaseFailureException;

/**
 * Handles access to stored customer data. In a real application this
 * class would communicate with a database; here it keeps a small
 * in-memory list of sample customers.
 *
 * <p>Searching for phone number {@code "000-DATABASE-FAIL"} always
 * triggers a {@link DatabaseFailureException}, simulating a situation
 * where the database server is unavailable.</p>
 */
public class CustomerRegistry {

    /**
     * Hardcoded phone number that always triggers a simulated database
     * failure, to demonstrate {@link DatabaseFailureException} handling.
     */
    public static final String DB_FAILURE_TRIGGER = "000-DATABASE-FAIL";

    private final List<CustomerDTO> customers = new ArrayList<>();

    /**
     * Package-private constructor, called only by {@link RegistryCreator}.
     */
    CustomerRegistry() {
        addSampleCustomers();
    }

    /**
     * Looks up a customer by phone number.
     *
     * @param phoneNumber The phone number to search for.
     * @return The matching {@link CustomerDTO}.
     * @throws CustomerNotFoundException if no customer with that phone
     *         number exists in the registry.
     * @throws DatabaseFailureException  if the database cannot be
     *         reached (simulated when the phone number equals
     *         {@value #DB_FAILURE_TRIGGER}).
     */
    public CustomerDTO findCustomer(String phoneNumber)
            throws CustomerNotFoundException {
        if (DB_FAILURE_TRIGGER.equals(phoneNumber)) {
            throw new DatabaseFailureException(
                    "Database server is not responding during customer lookup.",
                    new RuntimeException("Simulated connection timeout"));
        }
        for (CustomerDTO customer : customers) {
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                return customer;
            }
        }
        throw new CustomerNotFoundException(phoneNumber);
    }

    private void addSampleCustomers() {
        customers.add(new CustomerDTO("Tousif Dewan", "tsdewan@kth.se", "070-1234567"));
        customers.add(new CustomerDTO("Andreas Wissel", "andreas12@xyz.com", "072-1234567"));
    }
}
