package se.kth.iv1350.repairelectricbike.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairelectricbike.integration.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that the Observer pattern is correctly implemented on RepairOrder.
 */
class RepairOrderObserverTest {

    /** Spy implementation that records all notifications received. */
    private static class SpyObserver implements RepairOrderObserver {
        final List<RepairOrderDTO> received = new ArrayList<>();

        @Override
        public void repairOrderUpdated(RepairOrderDTO updatedOrder) {
            received.add(updatedOrder);
        }
    }

    private RepairOrder order;
    private SpyObserver spy;

    @BeforeEach
    void setUp() {
        CustomerDTO customer = new CustomerDTO("Test", "t@t.com", "070-0000001");
        BikeDTO bike = new BikeDTO("Brand", "Model", "SN-1");
        order = new RepairOrder(1, "Problem", customer, bike);
        spy = new SpyObserver();
        order.addObserver(spy);
    }

    @Test
    void observerIsNotifiedWhenDiagnosticResultIsAdded() {
        order.addDiagnosticResult("Battery dead");
        assertEquals(1, spy.received.size(),
                "Observer should be notified once when a diagnostic result is added");
    }

    @Test
    void observerIsNotifiedWhenRepairTaskIsAdded() {
        order.addRepairTask(new RepairTaskDTO("Fix chain", new Amount(200.0)));
        assertEquals(1, spy.received.size(),
                "Observer should be notified once when a repair task is added");
    }

    @Test
    void observerIsNotifiedWhenOrderIsUpdated() {
        order.update("New description");
        assertEquals(1, spy.received.size(),
                "Observer should be notified once when order is updated");
    }

    @Test
    void observerIsNotifiedWhenStateChanges() {
        order.setState(OrderState.ACCEPTED);
        assertEquals(1, spy.received.size(),
                "Observer should be notified once when state changes");
    }

    @Test
    void observerReceivesNonNullDTOSnapshot() {
        order.addDiagnosticResult("Loose brake");
        assertNotNull(spy.received.get(0),
                "Observer should receive a non-null DTO snapshot");
    }

    @Test
    void multipleObserversAllReceiveNotification() {
        SpyObserver spy2 = new SpyObserver();
        order.addObserver(spy2);
        order.setState(OrderState.REJECTED);
        assertEquals(1, spy.received.size(), "First observer should receive notification");
        assertEquals(1, spy2.received.size(), "Second observer should also receive notification");
    }

    @Test
    void observerNotNotifiedBeforeAnyUpdate() {
        assertEquals(0, spy.received.size(),
                "Observer should not be notified before any update occurs");
    }
}
