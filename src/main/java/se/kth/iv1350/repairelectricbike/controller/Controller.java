package se.kth.iv1350.repairelectricbike.controller;

import se.kth.iv1350.repairelectricbike.exception.CustomerNotFoundException;
import se.kth.iv1350.repairelectricbike.integration.BikeDTO;
import se.kth.iv1350.repairelectricbike.integration.CustomerDTO;
import se.kth.iv1350.repairelectricbike.integration.CustomerRegistry;
import se.kth.iv1350.repairelectricbike.integration.Printer;
import se.kth.iv1350.repairelectricbike.integration.RegistryCreator;
import se.kth.iv1350.repairelectricbike.integration.RepairOrderDTO;
import se.kth.iv1350.repairelectricbike.integration.RepairOrderRegistry;
import se.kth.iv1350.repairelectricbike.integration.RepairTaskDTO;
import se.kth.iv1350.repairelectricbike.model.OrderState;
import se.kth.iv1350.repairelectricbike.model.RepairOrder;
import se.kth.iv1350.repairelectricbike.model.RepairOrderObserver;

/**
 * This is the application's only controller class. All calls from the
 * view to the model pass through here.
 *
 * <p>The controller is responsible for persisting model state after
 * each mutation by calling
 * {@link RepairOrderRegistry#save(RepairOrderDTO)}. This keeps the
 * integration layer free of model objects.</p>
 *
 * <p>The currently active {@link RepairOrder} is kept in memory as a
 * field. In a real multi-order system this would be a map keyed by
 * order ID; for this single-active-order scenario a single field
 * suffices.</p>
 */
public class Controller {
    private final CustomerRegistry customerRegistry;
    private final RepairOrderRegistry repairOrderRegistry;
    private final Printer printer;
    private RepairOrder currentOrder;

    /**
     * Creates a new {@link Controller}.
     *
     * @param regCreator Used to obtain the registries.
     * @param printer    The printer used for repair order printouts.
     */
    public Controller(RegistryCreator regCreator, Printer printer) {
        this.customerRegistry = regCreator.getCustomerRegistry();
        this.repairOrderRegistry = regCreator.getRepairOrderRegistry();
        this.printer = printer;
    }

    /**
     * Registers an observer to receive notifications whenever any
     * repair order is updated.
     *
     * @param observer The observer to add.
     */
    public void addRepairOrderObserver(RepairOrderObserver observer) {
        repairOrderRegistry.addObserver(observer);
    }

    /**
     * Looks up a customer by phone number.
     *
     * @param phoneNumber The phone number to search for.
     * @return The matching {@link CustomerDTO}.
     * @throws CustomerNotFoundException if no customer with that phone
     *         number exists in the registry.
     */
    public CustomerDTO findCustomer(String phoneNumber)
            throws CustomerNotFoundException {
        return customerRegistry.findCustomer(phoneNumber);
    }

    /**
     * Creates a new repair order for the specified customer and bike,
     * saves a snapshot to the registry, and sets it as the current
     * active order.
     *
     * @param description Short description of the reported problem.
     * @param customer    The customer owning the bike.
     * @param bike        The bike being repaired.
     * @return A DTO describing the newly created repair order.
     */
    public RepairOrderDTO createRepairOrder(String description,
                                            CustomerDTO customer, BikeDTO bike) {
        int orderId = repairOrderRegistry.nextOrderId();
        currentOrder = new RepairOrder(orderId, description, customer, bike);
        for (RepairOrderObserver obs : repairOrderRegistry.getObservers()) {
            currentOrder.addObserver(obs);
        }
        currentOrder.setDiscountStrategy(
                repairOrderRegistry.discountFor(customer.getPhoneNumber()));
        RepairOrderDTO snapshot = currentOrder.toDTO();
        repairOrderRegistry.save(snapshot);
        return snapshot;
    }

    /**
     * Prints the repair order with the specified identifier.
     *
     * @param orderId The order to print.
     */
    public void printRepairOrder(int orderId) {
        loadOrder(orderId);
        if (currentOrder != null) {
            currentOrder.printRepairOrder(printer);
        }
    }

    /**
     * Retrieves all repair orders currently stored.
     *
     * @return An array of DTOs, one per stored order.
     */
    public RepairOrderDTO[] findAllRepairOrders() {
        return repairOrderRegistry.findAllRepairOrders();
    }

    /**
     * Records a diagnostic finding on the specified repair order.
     *
     * @param orderId          The order identifier.
     * @param diagnosticResult The finding to record.
     */
    public void addDiagnosticResult(int orderId, String diagnosticResult) {
        loadOrder(orderId);
        if (currentOrder != null) {
            currentOrder.addDiagnosticResult(diagnosticResult);
            repairOrderRegistry.save(currentOrder.toDTO());
        }
    }

    /**
     * Adds a repair task to the specified repair order.
     *
     * @param orderId The order identifier.
     * @param task    The task to add.
     */
    public void addRepairTask(int orderId, RepairTaskDTO task) {
        loadOrder(orderId);
        if (currentOrder != null) {
            currentOrder.addRepairTask(task);
            repairOrderRegistry.save(currentOrder.toDTO());
        }
    }

    /**
     * Updates the description of the specified repair order and marks
     * it as ready for customer approval.
     *
     * @param orderId        The order identifier.
     * @param newDescription The updated description.
     */
    public void updateRepairOrder(int orderId, String newDescription) {
        loadOrder(orderId);
        if (currentOrder != null) {
            currentOrder.update(newDescription);
            repairOrderRegistry.save(currentOrder.toDTO());
        }
    }

    /**
     * Marks the specified repair order as accepted by the customer.
     *
     * @param orderId The order identifier.
     */
    public void acceptRepairOrder(int orderId) {
        loadOrder(orderId);
        if (currentOrder != null) {
            currentOrder.setState(OrderState.ACCEPTED);
            repairOrderRegistry.save(currentOrder.toDTO());
        }
    }

    /**
     * Marks the specified repair order as rejected by the customer.
     *
     * @param orderId The order identifier.
     */
    public void rejectRepairOrder(int orderId) {
        loadOrder(orderId);
        if (currentOrder != null) {
            currentOrder.setState(OrderState.REJECTED);
            repairOrderRegistry.save(currentOrder.toDTO());
        }
    }

    /**
     * Loads the order with the given ID into {@code currentOrder}.
     * If the requested ID matches the in-memory current order, no
     * registry lookup is needed. Otherwise the registry is queried and
     * a fresh {@link RepairOrder} is reconstructed from the stored DTO.
     *
     * <p>Note: reconstructing from a DTO loses the diagnostic report
     * and task list detail that is not yet in the DTO. In a production
     * system the full object would be serialised; here this is a
     * known limitation of the in-memory simulation.</p>
     *
     * @param orderId The order to load.
     */
    private void loadOrder(int orderId) {
        if (currentOrder != null && currentOrder.getOrderId() == orderId) {
            return; // already in memory
        }
        RepairOrderDTO dto = repairOrderRegistry.findRepairOrderDTO(orderId);
        if (dto == null) {
            currentOrder = null;
            return;
        }
        currentOrder = new RepairOrder(dto.getOrderId(), dto.getDescription(),
                dto.getCustomer(), dto.getBike());
        for (RepairOrderObserver obs : repairOrderRegistry.getObservers()) {
            currentOrder.addObserver(obs);
        }
    }
}
