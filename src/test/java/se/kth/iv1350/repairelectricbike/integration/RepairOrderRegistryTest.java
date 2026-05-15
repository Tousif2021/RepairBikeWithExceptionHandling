package se.kth.iv1350.repairelectricbike.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairelectricbike.exception.DatabaseFailureException;
import se.kth.iv1350.repairelectricbike.model.Amount;
import se.kth.iv1350.repairelectricbike.model.discount.LoyaltyDiscount;
import se.kth.iv1350.repairelectricbike.model.discount.NoDiscount;

import static org.junit.jupiter.api.Assertions.*;

class RepairOrderRegistryTest {

    private RepairOrderRegistry registry;
    private CustomerDTO customer;
    private BikeDTO bike;

    @BeforeEach
    void setUp() {
        registry = new RepairOrderRegistry();
        customer = new CustomerDTO("Test User", "test@test.com", "070-9999999");
        bike = new BikeDTO("TestBrand", "TestModel", "SN-001");
    }

    @Test
    void nextOrderIdStartsAtOne() {
        assertEquals(1, registry.nextOrderId(),
                "First allocated order ID should be 1");
    }

    @Test
    void nextOrderIdIncrementsOnEachCall() {
        int first = registry.nextOrderId();
        int second = registry.nextOrderId();
        assertEquals(first + 1, second,
                "Each call to nextOrderId() should return the next integer");
    }

    @Test
    void findRepairOrderDTOThrowsDatabaseFailureForTriggerID() {
        assertThrows(DatabaseFailureException.class,
                () -> registry.findRepairOrderDTO(RepairOrderRegistry.DB_FAILURE_TRIGGER_ID),
                "Should throw DatabaseFailureException for trigger order ID");
    }

    @Test
    void findRepairOrderDTOReturnsNullForUnknownId() {
        assertNull(registry.findRepairOrderDTO(9999),
                "Should return null for an order ID that does not exist");
    }

    @Test
    void findAllRepairOrdersReturnsEmptyArrayWhenNoOrdersExist() {
        assertEquals(0, registry.findAllRepairOrders().length,
                "Fresh registry should contain zero orders");
    }

    @Test
    void saveAddsNewOrderWhenIdDoesNotExist() {
        RepairOrderDTO dto = new RepairOrderDTO(1, "desc", "CREATED",
                customer, bike, new Amount(0));
        registry.save(dto);
        assertEquals(1, registry.findAllRepairOrders().length,
                "Registry should contain one order after saving one");
    }

    @Test
    void saveReplacesExistingOrderWithSameId() {
        RepairOrderDTO first = new RepairOrderDTO(1, "original", "CREATED",
                customer, bike, new Amount(0));
        RepairOrderDTO updated = new RepairOrderDTO(1, "updated", "ACCEPTED",
                customer, bike, new Amount(500));
        registry.save(first);
        registry.save(updated);
        assertEquals(1, registry.findAllRepairOrders().length,
                "Saving with same ID should replace, not append");
        assertEquals("updated", registry.findRepairOrderDTO(1).getDescription(),
                "Description should reflect the updated snapshot");
    }

    @Test
    void discountForReturnsNoDiscountForFirstOrder() {
        assertTrue(registry.discountFor(customer.getPhoneNumber()) instanceof NoDiscount,
                "First order for a customer should receive NoDiscount");
    }

    @Test
    void discountForReturnsLoyaltyDiscountForThirdOrder() {
        // Simulate two previous orders for this customer in the registry
        RepairOrderDTO dto1 = new RepairOrderDTO(1, "s1", "CREATED", customer, bike, new Amount(0));
        RepairOrderDTO dto2 = new RepairOrderDTO(2, "s2", "CREATED", customer, bike, new Amount(0));
        registry.save(dto1);
        registry.save(dto2);
        assertTrue(registry.discountFor(customer.getPhoneNumber()) instanceof LoyaltyDiscount,
                "Third order for same customer should receive LoyaltyDiscount");
    }
}
