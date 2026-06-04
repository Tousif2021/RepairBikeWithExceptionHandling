package se.kth.iv1350.repairelectricbike.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairelectricbike.integration.BikeDTO;
import se.kth.iv1350.repairelectricbike.integration.CustomerDTO;
import se.kth.iv1350.repairelectricbike.integration.RepairOrderDTO;
import se.kth.iv1350.repairelectricbike.model.Amount;
import se.kth.iv1350.repairelectricbike.view.observer.RepairOrderView;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link RepairOrderView}. Verifies that every
 * informational field of a repair order is printed to
 * {@code System.out} when {@code repairOrderUpdated} is called.
 */
class RepairOrderViewTest {

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private RepairOrderView view;
    private RepairOrderDTO sampleDTO;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        view = new RepairOrderView();

        CustomerDTO customer = new CustomerDTO(
                "Tousif Dewan", "tsdewan@kth.se", "070-1234567");
        BikeDTO bike = new BikeDTO("Kawasaki", "Pro Mountain Bike", "0012");
        sampleDTO = new RepairOrderDTO(
                1, "Battery will not charge", "CREATED",
                customer, bike, new Amount(3950.00));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private String output() {
        return outContent.toString();
    }

    @Test
    void repairOrderUpdated_printsOrderId() {
        view.repairOrderUpdated(sampleDTO);
        assertTrue(output().contains("1"),
                "Output should contain the order ID");
    }

    @Test
    void repairOrderUpdated_printsCustomerName() {
        view.repairOrderUpdated(sampleDTO);
        assertTrue(output().contains("Tousif Dewan"),
                "Output should contain the customer name");
    }

    @Test
    void repairOrderUpdated_printsBikeInfo() {
        view.repairOrderUpdated(sampleDTO);
        assertTrue(output().contains("Kawasaki"),
                "Output should contain the bike brand");
        assertTrue(output().contains("Pro Mountain Bike"),
                "Output should contain the bike model");
    }

    @Test
    void repairOrderUpdated_printsTotalCostInSEK() {
        view.repairOrderUpdated(sampleDTO);
        assertTrue(output().contains("3950"),
                "Output should contain the total cost");
        assertTrue(output().contains("SEK"),
                "Output should contain the currency label SEK");
    }

    @Test
    void repairOrderUpdated_printsState() {
        view.repairOrderUpdated(sampleDTO);
        assertTrue(output().contains("CREATED"),
                "Output should contain the order state");
    }

    @Test
    void repairOrderUpdated_printsDescription() {
        view.repairOrderUpdated(sampleDTO);
        assertTrue(output().contains("Battery will not charge"),
                "Output should contain the order description");
    }

    @Test
    void repairOrderUpdated_doesNotPrintUnrelatedContent() {
        view.repairOrderUpdated(sampleDTO);
        assertFalse(output().contains("Andreas Wissel"),
                "Output should not contain a different customer's name");
    }
}