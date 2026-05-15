package se.kth.iv1350.repairelectricbike.integration;

import se.kth.iv1350.repairelectricbike.model.RepairOrder;

/**
 * The interface to the printer, used for all printouts initiated by
 * this program.
 */
public class Printer {

    /**
     * Prints the specified repair order to {@link System#out}. In a real
     * application this would send the printout to a physical printer.
     *
     * @param repairOrder The repair order to print.
     */
    public void printRepairOrder(RepairOrder repairOrder) {
        StringBuilder printout = new StringBuilder();
        printout.append("\n=============== REPAIR ORDER ===============\n");
        printout.append("Order ID:     ").append(repairOrder.getOrderId()).append("\n");
        printout.append("State:        ").append(repairOrder.getState()).append("\n");
        printout.append("Description:  ").append(repairOrder.getDescription()).append("\n");
        printout.append("--- Customer ---\n");
        printout.append(repairOrder.getCustomer().getName()).append("\n");
        printout.append(repairOrder.getCustomer().getEmail()).append("\n");
        printout.append(repairOrder.getCustomer().getPhoneNumber()).append("\n");
        printout.append("--- Bike ---\n");
        printout.append(repairOrder.getBike().getBrand()).append(" ");
        printout.append(repairOrder.getBike().getModel()).append("\n");
        printout.append("Serial no:    ").append(repairOrder.getBike().getSerialNo()).append("\n");
        printout.append("Total cost:   ").append(repairOrder.getTotalCost()).append("\n");
        printout.append("============================================\n");
        System.out.println(printout);
    }
}
