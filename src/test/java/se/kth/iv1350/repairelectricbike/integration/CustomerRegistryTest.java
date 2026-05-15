package se.kth.iv1350.repairelectricbike.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairelectricbike.exception.CustomerNotFoundException;
import se.kth.iv1350.repairelectricbike.exception.DatabaseFailureException;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRegistryTest {

    private CustomerRegistry registry;

    @BeforeEach
    void setUp() {
        registry = RegistryCreator.getInstance().getCustomerRegistry();
    }

    @Test
    void findCustomerReturnsCorrectCustomerForKnownPhoneNumber()
            throws CustomerNotFoundException {
        CustomerDTO result = registry.findCustomer("070-1234567");
        assertEquals("Tousif Dewan", result.getName(),
                "Should return the customer whose phone number was searched for");
    }

    @Test
    void findCustomerThrowsCustomerNotFoundExceptionForUnknownNumber() {
        assertThrows(CustomerNotFoundException.class,
                () -> registry.findCustomer("000-0000000"),
                "Should throw CustomerNotFoundException for a number not in the registry");
    }

    @Test
    void customerNotFoundExceptionContainsSearchedPhoneNumber() {
        String searched = "000-0000000";
        CustomerNotFoundException ex = assertThrows(
                CustomerNotFoundException.class,
                () -> registry.findCustomer(searched));
        assertEquals(searched, ex.getPhoneNumber(),
                "Exception should carry the phone number that was not found");
    }

    @Test
    void findCustomerThrowsDatabaseFailureExceptionForTriggerNumber() {
        assertThrows(DatabaseFailureException.class,
                () -> registry.findCustomer(CustomerRegistry.DB_FAILURE_TRIGGER),
                "Should throw DatabaseFailureException for the hardcoded trigger number");
    }

    @Test
    void findCustomerDoesNotThrowForSecondKnownCustomer()
            throws CustomerNotFoundException {
        CustomerDTO result = registry.findCustomer("072-1234567");
        assertEquals("Andreas Wissel", result.getName(),
                "Second sample customer should also be found without exception");
    }
}
