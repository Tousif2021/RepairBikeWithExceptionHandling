package se.kth.iv1350.repairelectricbike.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Collects diagnostic findings recorded by the technician for a single
 * repair order.
 */
public class DiagnosticReport {
    private final List<String> findings = new ArrayList<>();

    /**
     * Adds a diagnostic finding to the report.
     *
     * @param finding A short description of the finding.
     */
    public void addFinding(String finding) {
        findings.add(finding);
    }

    /**
     * @return An unmodifiable view of all findings in the order they
     *         were recorded.
     */
    public List<String> getFindings() {
        return Collections.unmodifiableList(findings);
    }
}
