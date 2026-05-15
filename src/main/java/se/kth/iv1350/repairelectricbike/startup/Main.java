package se.kth.iv1350.repairelectricbike.startup;

import se.kth.iv1350.repairelectricbike.controller.Controller;
import se.kth.iv1350.repairelectricbike.integration.Printer;
import se.kth.iv1350.repairelectricbike.integration.RegistryCreator;
import se.kth.iv1350.repairelectricbike.view.View;

/**
 * Starts the application by instantiating the central objects in each
 * layer and then handing control over to the view.
 *
 * <p>{@link RegistryCreator#getInstance()} is called here instead of
 * {@code new RegistryCreator()}, respecting the Singleton contract.</p>
 */
public class Main {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments, currently unused.
     */
    public static void main(String[] args) {
        RegistryCreator regCreator = RegistryCreator.getInstance();
        Printer printer = new Printer();
        Controller controller = new Controller(regCreator, printer);
        View view = new View(controller);
        view.execution();
    }
}
