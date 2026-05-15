package se.kth.iv1350.repairelectricbike.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairelectricbike.integration.BikeDTO;
import se.kth.iv1350.repairelectricbike.integration.CustomerDTO;
import se.kth.iv1350.repairelectricbike.integration.RepairOrderDTO;
import se.kth.iv1350.repairelectricbike.integration.RepairTaskDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link RepairOrder} class. Covers state transitions,
 * diagnostic recording, repair task accumulation, total cost
 * calculation, DTO creation, and encapsulation.
 */
public class RepairOrderTest {
    private CustomerDTO customer;
    private BikeDTO bike;
    private RepairOrder instance;

    @BeforeEach
    public void setUp() {
        customer = new CustomerDTO("Test Customer", "test@example.com", "070-0000000");
        bike = new BikeDTO("TestBrand", "TestModel", "SN-1");
        instance = new RepairOrder(42, "Initial problem", customer, bike);
    }

    @Test
    public void newOrderIsInCreatedState() {
        assertEquals(OrderState.CREATED, instance.getState(),
                "A newly created order should be in state CREATED.");
    }

    @Test
    public void constructorStoresAllFields() {
        assertEquals(42, instance.getOrderId());
        assertEquals("Initial problem", instance.getDescription());
        assertEquals(customer, instance.getCustomer());
        assertEquals(bike, instance.getBike());
    }

    @Test
    public void addDiagnosticResultStoresFinding() {
        instance.addDiagnosticResult("Battery dead");
        assertEquals(1, instance.getDiagnosticReport().getFindings().size(),
                "A diagnostic finding should have been recorded.");
        assertEquals("Battery dead",
                instance.getDiagnosticReport().getFindings().get(0));
    }

    @Test
    public void multipleDiagnosticResultsAreAllStored() {
        instance.addDiagnosticResult("Finding A");
        instance.addDiagnosticResult("Finding B");
        assertEquals(2, instance.getDiagnosticReport().getFindings().size());
    }

    @Test
    public void addRepairTaskAddsTaskToOrder() {
        RepairTaskDTO taskDTO = new RepairTaskDTO("Replace battery", new Amount(3500));
        instance.addRepairTask(taskDTO);
        assertEquals(1, instance.getRepairTasks().size(),
                "The repair task should have been added.");
        assertEquals("Replace battery",
                instance.getRepairTasks().get(0).getDescription());
    }

    @Test
    public void getTotalCostReturnsZeroWhenNoTasks() {
        assertEquals(0.0, instance.getTotalCost().getAmount(), 0.0001,
                "Total cost should be zero when no repair tasks have been added.");
    }

    @Test
    public void getTotalCostSumsAllTaskCosts() {
        instance.addRepairTask(new RepairTaskDTO("Task 1", new Amount(100)));
        instance.addRepairTask(new RepairTaskDTO("Task 2", new Amount(200.50)));
        instance.addRepairTask(new RepairTaskDTO("Task 3", new Amount(50.25)));
        assertEquals(350.75, instance.getTotalCost().getAmount(), 0.0001,
                "Total cost should be the sum of all task costs.");
    }

    @Test
    public void updateChangesDescriptionAndState() {
        instance.update("Updated problem description");
        assertEquals("Updated problem description", instance.getDescription());
        assertEquals(OrderState.READY_FOR_APPROVAL, instance.getState(),
                "update() should transition the state to READY_FOR_APPROVAL.");
    }

    @Test
    public void setStateChangesTheStateToAccepted() {
        instance.setState(OrderState.ACCEPTED);
        assertEquals(OrderState.ACCEPTED, instance.getState());
    }

    @Test
    public void setStateChangesTheStateToRejected() {
        instance.setState(OrderState.REJECTED);
        assertEquals(OrderState.REJECTED, instance.getState());
    }

    @Test
    public void toDTOReturnsSnapshotWithMatchingData() {
        instance.addRepairTask(new RepairTaskDTO("Task", new Amount(500)));
        RepairOrderDTO dto = instance.toDTO();
        assertEquals(42, dto.getOrderId());
        assertEquals("Initial problem", dto.getDescription());
        assertEquals("CREATED", dto.getState());
        assertEquals(customer, dto.getCustomer());
        assertEquals(bike, dto.getBike());
        assertEquals(500.0, dto.getTotalCost().getAmount(), 0.0001);
    }

    @Test
    public void toDTOReflectsStateAfterTransition() {
        instance.setState(OrderState.ACCEPTED);
        RepairOrderDTO dto = instance.toDTO();
        assertEquals("ACCEPTED", dto.getState(),
                "DTO state should reflect the current state of the order.");
    }

    @Test
    public void getRepairTasksReturnsUnmodifiableList() {
        instance.addRepairTask(new RepairTaskDTO("Task", new Amount(10)));
        assertThrows(UnsupportedOperationException.class,
                () -> instance.getRepairTasks().clear(),
                "The repair task list must be unmodifiable from outside.");
    }

    @Test
    public void fullLifecycleLeavesOrderInAcceptedState() {
        instance.addDiagnosticResult("Finding");
        instance.addRepairTask(new RepairTaskDTO("Fix", new Amount(100)));
        instance.update("Refined description");
        instance.setState(OrderState.ACCEPTED);

        assertEquals(OrderState.ACCEPTED, instance.getState());
        assertEquals("Refined description", instance.getDescription());
        assertEquals(100.0, instance.getTotalCost().getAmount(), 0.0001);
        assertTrue(instance.getDiagnosticReport().getFindings().contains("Finding"));
    }
}
