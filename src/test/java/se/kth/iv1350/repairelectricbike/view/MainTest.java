package se.kth.iv1350.repairelectricbike.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairelectricbike.startup.Main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that {@link Main#main(String[])} runs without throwing an
 * uncaught exception and produces output to {@code System.out}.
 *
 * <p>The stronger content checks are in {@link ViewTest}, which tests
 * each scenario in isolation. This class verifies only that the
 * application starts and completes without error.</p>
 */
class MainTest {

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

    @Test
    void main_producesOutput() {
        Main.main(new String[]{});
        assertFalse(outContent.toString().isEmpty(),
                "Main should produce output to System.out");
    }

    @Test
    void main_doesNotThrowException() {
        assertDoesNotThrow(() -> Main.main(new String[]{}),
                "Main should not throw any uncaught exception");
    }
}