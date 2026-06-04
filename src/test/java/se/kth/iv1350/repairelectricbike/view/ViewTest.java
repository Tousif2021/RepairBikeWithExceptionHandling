package se.kth.iv1350.repairelectricbike.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairelectricbike.controller.Controller;
import se.kth.iv1350.repairelectricbike.integration.Printer;
import se.kth.iv1350.repairelectricbike.integration.RegistryCreator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests all informational printouts produced by {@link View#execution()}.
 *
 * <p>Each test builds a real object graph and calls
 * {@code view.execution()}, then checks that the relevant scenario
 * produced the expected output on {@code System.out}. Purely decorative
 * separator lines are not individually tested.</p>
 */
class ViewTest {

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private String output() {
        return outContent.toString();
    }

    private View buildView() {
        RegistryCreator regCreator = RegistryCreator.getInstance();
        Printer printer = new Printer();
        Controller controller = new Controller(regCreator, printer);
        return new View(controller);
    }

    // ── Happy path ───────────────────────────────────────────────

    @Test
    void execution_happyPath_printsFoundCustomer() {
        buildView().execution();
        assertTrue(output().contains("Tousif Dewan"),
                "Happy path should print the found customer name");
    }

    @Test
    void execution_happyPath_printsCreatedOrder() {
        buildView().execution();
        // Check for "Order #" followed by any number and the description,
        // so the test is not sensitive to which order ID the Singleton assigns
        assertTrue(output().contains("Order #"),
                "Happy path should print a created repair order");
        assertTrue(output().contains("Battery will not charge"),
                "Happy path should print the order description");
    }

    @Test
    void execution_happyPath_printsFinalReceipt() {
        buildView().execution();
        assertTrue(output().contains("REPAIR ORDER"),
                "Happy path should print the final repair order receipt");
    }

    @Test
    void execution_happyPath_printsTotalCostOnReceipt() {
        buildView().execution();
        // The receipt always contains "Total cost:" regardless of order ID
        assertTrue(output().contains("Total cost:"),
                "Final receipt should contain a total cost line");
    }

    // ── Alternative flow 5a: customer not found ──────────────────

    @Test
    void execution_altFlow5a_printsPhoneNumberInErrorMessage() {
        buildView().execution();
        assertTrue(output().contains("000-0000000"),
                "Alt flow error message should contain the searched phone number");
    }

    @Test
    void execution_altFlow5a_doesNotExposeExceptionClassName() {
        buildView().execution();
        assertFalse(output().contains("CustomerNotFoundException"),
                "User-facing error must not expose the exception class name");
    }

    // ── Database failure ─────────────────────────────────────────

    @Test
    void execution_databaseFailure_printsUnavailableMessage() {
        buildView().execution();
        assertTrue(output().contains("temporarily unavailable"),
                "Database failure should show a generic unavailable message");
    }

    @Test
    void execution_databaseFailure_doesNotExposeExceptionClassName() {
        buildView().execution();
        assertFalse(output().contains("DatabaseFailureException"),
                "User-facing database error must not expose the exception class name");
    }

    // ── Loyalty discount ─────────────────────────────────────────

    @Test
    void execution_loyaltyDiscount_thirdOrderShowsDiscountedTotal() {
        buildView().execution();
        assertTrue(output().contains("900.00"),
                "Third loyalty order should show discounted total of 900.00 SEK");
    }

    @Test
    void execution_loyaltyDiscount_firstOrderIsFullPrice() {
        buildView().execution();
        assertTrue(output().contains("1000.00"),
                "First loyalty order should show full price of 1000.00 SEK");
    }
}