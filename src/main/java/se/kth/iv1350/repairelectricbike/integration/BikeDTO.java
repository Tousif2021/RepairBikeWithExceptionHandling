package se.kth.iv1350.repairelectricbike.integration;

/**
 * Immutable data transfer object describing an electric bike brought in
 * for repair.
 */
public final class BikeDTO {
    private final String brand;
    private final String model;
    private final String serialNo;

    /**
     * Creates a new {@link BikeDTO}.
     *
     * @param brand    The bike manufacturer.
     * @param model    The bike model designation.
     * @param serialNo The bike's unique serial number.
     */
    public BikeDTO(String brand, String model, String serialNo) {
        this.brand = brand;
        this.model = model;
        this.serialNo = serialNo;
    }

    /**
     * @return The bike brand.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @return The bike model.
     */
    public String getModel() {
        return model;
    }

    /**
     * @return The bike serial number.
     */
    public String getSerialNo() {
        return serialNo;
    }
}
