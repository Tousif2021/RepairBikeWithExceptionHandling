package se.kth.iv1350.repairelectricbike.integration;

import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairelectricbike.exception.CustomerNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link RegistryCreator} Singleton.
 */
class RegistryCreatorTest {

    @Test
    void getInstanceReturnsNonNull() {
        assertNotNull(RegistryCreator.getInstance(),
                "getInstance() must return a non-null object");
    }

    @Test
    void getInstanceReturnsSameObjectOnRepeatedCalls() {
        RegistryCreator first = RegistryCreator.getInstance();
        RegistryCreator second = RegistryCreator.getInstance();
        assertSame(first, second,
                "getInstance() must return the same instance (Singleton contract)");
    }

    @Test
    void getCustomerRegistryReturnsNonNull() {
        assertNotNull(RegistryCreator.getInstance().getCustomerRegistry(),
                "getCustomerRegistry() must return a valid registry");
    }

    @Test
    void getRepairOrderRegistryReturnsNonNull() {
        assertNotNull(RegistryCreator.getInstance().getRepairOrderRegistry(),
                "getRepairOrderRegistry() must return a valid registry");
    }

    @Test
    void customerRegistryIsFullyInitializedAfterCreation()
            throws CustomerNotFoundException {
        CustomerDTO customer = RegistryCreator.getInstance()
                .getCustomerRegistry().findCustomer("070-1234567");
        assertNotNull(customer,
                "Registry returned by RegistryCreator must be fully populated");
    }
}
