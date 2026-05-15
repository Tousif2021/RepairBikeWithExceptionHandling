package se.kth.iv1350.repairelectricbike.model;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link DiagnosticReport} class. Verifies that findings
 * are stored in insertion order and that the returned list is
 * unmodifiable.
 */
public class DiagnosticReportTest {
    private DiagnosticReport instance;

    @BeforeEach
    public void setUp() {
        instance = new DiagnosticReport();
    }

    @Test
    public void newReportHasNoFindings() {
        assertTrue(instance.getFindings().isEmpty(),
                "A new diagnostic report should have no findings.");
    }

    @Test
    public void addFindingStoresTheFinding() {
        instance.addFinding("Battery cells are dead");
        List<String> findings = instance.getFindings();
        assertEquals(1, findings.size(), "Exactly one finding should be stored.");
        assertEquals("Battery cells are dead", findings.get(0),
                "Stored finding should match the added text.");
    }

    @Test
    public void findingsAreKeptInInsertionOrder() {
        instance.addFinding("First");
        instance.addFinding("Second");
        instance.addFinding("Third");
        List<String> findings = instance.getFindings();
        assertEquals("First", findings.get(0));
        assertEquals("Second", findings.get(1));
        assertEquals("Third", findings.get(2));
    }

    @Test
    public void getFindingsReturnsUnmodifiableList() {
        instance.addFinding("Some finding");
        List<String> findings = instance.getFindings();
        assertThrows(UnsupportedOperationException.class,
                () -> findings.add("Another finding"),
                "The list returned by getFindings must be unmodifiable "
                        + "to preserve encapsulation.");
    }
}
