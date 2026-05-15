package se.kth.iv1350.repairelectricbike.model;

import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairelectricbike.integration.RepairTaskDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link RepairTask} class. Verifies that a task is
 * correctly constructed from a DTO.
 */
public class RepairTaskTest {

    @Test
    public void constructorCopiesDescriptionFromDTO() {
        RepairTaskDTO dto = new RepairTaskDTO("Replace chain", new Amount(250));
        RepairTask task = new RepairTask(dto);
        assertEquals("Replace chain", task.getDescription(),
                "Description should be copied from the DTO.");
    }

    @Test
    public void constructorCopiesCostFromDTO() {
        RepairTaskDTO dto = new RepairTaskDTO("Replace chain", new Amount(250));
        RepairTask task = new RepairTask(dto);
        assertEquals(250.0, task.getCost().getAmount(), 0.0001,
                "Cost should be copied from the DTO.");
    }

    @Test
    public void twoTasksFromDifferentDTOsAreIndependent() {
        RepairTask task1 = new RepairTask(new RepairTaskDTO("Task A", new Amount(10)));
        RepairTask task2 = new RepairTask(new RepairTaskDTO("Task B", new Amount(20)));
        assertEquals("Task A", task1.getDescription());
        assertEquals("Task B", task2.getDescription());
        assertEquals(10.0, task1.getCost().getAmount(), 0.0001);
        assertEquals(20.0, task2.getCost().getAmount(), 0.0001);
    }
}
