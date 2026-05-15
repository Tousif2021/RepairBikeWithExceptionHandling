package se.kth.iv1350.repairelectricbike.integration;

/**
 * Responsible for instantiating all registries. Hides the existence of
 * individual registry classes from the startup and controller layers.
 *
 * <p>Implemented as a <em>Singleton</em>: only one instance can exist
 * in the application. This guarantees that all parts of the program
 * share the same registry instances, which prevents a second
 * {@link CustomerRegistry} or {@link RepairOrderRegistry} from being
 * accidentally created with a separate, empty data set.</p>
 */
public class RegistryCreator {

    private static RegistryCreator instance;

    private final CustomerRegistry customerRegistry = new CustomerRegistry();
    private final RepairOrderRegistry repairOrderRegistry = new RepairOrderRegistry();

    /**
     * Private constructor — use {@link #getInstance()} instead.
     */
    private RegistryCreator() {
    }

    /**
     * Returns the single shared instance of {@link RegistryCreator},
     * creating it on the first call.
     *
     * @return The singleton instance.
     */
    public static RegistryCreator getInstance() {
        if (instance == null) {
            instance = new RegistryCreator();
        }
        return instance;
    }

    /**
     * @return The {@link CustomerRegistry}.
     */
    public CustomerRegistry getCustomerRegistry() {
        return customerRegistry;
    }

    /**
     * @return The {@link RepairOrderRegistry}.
     */
    public RepairOrderRegistry getRepairOrderRegistry() {
        return repairOrderRegistry;
    }
}
