package se.kth.iv1350.repairelectricbike.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairelectricbike.integration.BikeDTO;
import se.kth.iv1350.repairelectricbike.integration.CustomerDTO;
import se.kth.iv1350.repairelectricbike.integration.Printer;
import se.kth.iv1350.repairelectricbike.integration.RepairTaskDTO;
import se.kth.iv1350.repairelectricbike.model.Amount;
import se.kth.iv1350.repairelectricbike.model.RepairOrder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Printer}. Verifies that all informational
 * fields of a repair order appear in the printout sent to
 * {@code System.out}.
 */
class PrinterTest {

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private Printer printer;
    private RepairOrder repairOrder;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        printer = new Printer();

        CustomerDTO customer = new CustomerDTO(
                "Tousif Dewan", "tsdewan@kth.se", "070-1234567");
        BikeDTO bike = new BikeDTO("Kawasaki", "Pro Mountain Bike", "0012");
        repairOrder = new RepairOrder(1, "Battery will not charge", customer, bike);
        repairOrder.addRepairTask(
                new RepairTaskDTO("Replace battery pack", new Amount(3500.00)));
        repairOrder.addRepairTask(
                new RepairTaskDTO("Fix charger port", new Amount(450.00)));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private String output() {
        return outContent.toString();
    }

    @Test
    void printRepairOrder_containsOrderId() {
        printer.printRepairOrder(repairOrder);
        assertTrue(output().contains("1"),
                "Printout should contain the order ID");
    }

    @Test
    void printRepairOrder_containsCustomerName() {
        printer.printRepairOrder(repairOrder);
        assertTrue(output().contains("Tousif Dewan"),
                "Printout should contain the customer name");
    }

    @Test
    void printRepairOrder_containsCustomerEmail() {
        printer.printRepairOrder(repairOrder);
        assertTrue(output().contains("tsdewan@kth.se"),
                "Printout should contain the customer email");
    }

    @Test
    void printRepairOrder_containsBikeInfo() {
        printer.printRepairOrder(repairOrder);
        assertTrue(output().contains("Kawasaki"),
                "Printout should contain the bike brand");
        assertTrue(output().contains("Pro Mountain Bike"),
                "Printout should contain the bike model");
        assertTrue(output().contains("0012"),
                "Printout should contain the bike serial number");
    }

    @Test
    void printRepairOrder_containsTotalCost() {
        printer.printRepairOrder(repairOrder);
        assertTrue(output().contains("3950"),
                "Printout should contain the total cost (3500 + 450 = 3950)");
    }

    @Test
    void printRepairOrder_containsDescription() {
        printer.printRepairOrder(repairOrder);
        assertTrue(output().contains("Battery will not charge"),
                "Printout should contain the order description");
    }

    @Test
    void printRepairOrder_doesNotPrintUnrelatedOrder() {
        printer.printRepairOrder(repairOrder);
        assertFalse(output().contains("Order ID:     2"),
                "Printout should not contain a different order ID");
    }
}