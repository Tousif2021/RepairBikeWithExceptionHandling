package se.kth.iv1350.repairelectricbike.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairelectricbike.exception.CustomerNotFoundException;
import se.kth.iv1350.repairelectricbike.exception.DatabaseFailureException;
import se.kth.iv1350.repairelectricbike.integration.*;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private Controller controller;

    @BeforeEach
    void setUp() {
        controller = new Controller(RegistryCreator.getInstance(), new Printer());
    }

    @Test
    void findCustomerReturnsCustomerForKnownPhoneNumber()
            throws CustomerNotFoundException {
        CustomerDTO result = controller.findCustomer("070-1234567");
        assertNotNull(result, "Should return a non-null customer for a known number");
    }

    @Test
    void findCustomerThrowsCustomerNotFoundForUnknownNumber() {
        assertThrows(CustomerNotFoundException.class,
                () -> controller.findCustomer("000-0000000"),
                "Controller should propagate CustomerNotFoundException");
    }

    @Test
    void findCustomerThrowsDatabaseFailureForTriggerNumber() {
        assertThrows(DatabaseFailureException.class,
                () -> controller.findCustomer(CustomerRegistry.DB_FAILURE_TRIGGER),
                "Controller should propagate DatabaseFailureException");
    }

    @Test
    void createRepairOrderReturnsNonNullDTO() throws CustomerNotFoundException {
        CustomerDTO customer = controller.findCustomer("070-1234567");
        BikeDTO bike = new BikeDTO("Brand", "Model", "SN-1");
        RepairOrderDTO dto = controller.createRepairOrder("Problem", customer, bike);
        assertNotNull(dto, "Created order should not be null");
    }

    @Test
    void printRepairOrderThrowsDatabaseFailureForTriggerID() {
        assertThrows(DatabaseFailureException.class,
                () -> controller.printRepairOrder(
                        RepairOrderRegistry.DB_FAILURE_TRIGGER_ID),
                "Controller should propagate DatabaseFailureException from registry");
    }

    @Test
    void addDiagnosticResultThrowsDatabaseFailureForTriggerID() {
        assertThrows(DatabaseFailureException.class,
                () -> controller.addDiagnosticResult(
                        RepairOrderRegistry.DB_FAILURE_TRIGGER_ID, "Finding"),
                "addDiagnosticResult should propagate DatabaseFailureException");
    }
}
