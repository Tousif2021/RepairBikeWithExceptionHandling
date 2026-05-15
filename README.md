# Repair Electric Bike — IV1350 Seminar 4

Extension of the Seminar 3 implementation adding **exception handling**, the **Observer pattern**, the **Singleton pattern**, and the **Strategy pattern**.

**Course:** IV1350 Object-Oriented Design, KTH  
**Author:** Tousif Dewan (`tsdewan@kth.se`)

---

## What Was Added (Seminar 4)

### Task 1 — Exception Handling
- `CustomerNotFoundException` (checked) — thrown when a phone number is not found
- `DatabaseFailureException` (unchecked) — thrown when the DB is unavailable
  - Trigger: search phone `000-DATABASE-FAIL` or order ID `999`
- View catches both: shows user message + logs `DatabaseFailureException` to `error-log.txt`
- Unit tests in `CustomerRegistryTest`, `RepairOrderRegistryTest`, `ControllerTest`

### Task 2a — Observer Pattern
- `RepairOrderObserver` interface in `model` package
- `RepairOrder` is the observed object — notifies after every mutation
- `RepairOrderView` — prints updates to `System.out`
- `RepairOrderLogger` — appends updates to `repair-order-log.txt`
- Observers registered in `View` constructor via `Controller.addRepairOrderObserver()`
- Unit tests in `RepairOrderObserverTest`

### Task 2b — Two Extra GoF Patterns
- **Singleton** — `RegistryCreator` ensures only one registry instance exists
- **Strategy** — `DiscountStrategy` interface with `NoDiscount` and `LoyaltyDiscount` (10% off every 3rd order per customer)
- Unit tests in `DiscountStrategyTest`, `RepairOrderRegistryTest`

---

## Project Structure

```
src/main/java/.../repairelectricbike/
├── startup/        Main.java
├── view/           View.java, ErrorLogger.java
│   └── observer/   RepairOrderView.java, RepairOrderLogger.java
├── controller/     Controller.java
├── model/          RepairOrder.java, RepairOrderObserver.java, ...
│   └── discount/   DiscountStrategy.java, NoDiscount.java, LoyaltyDiscount.java
├── integration/    CustomerRegistry.java, RepairOrderRegistry.java, ...
└── exception/      CustomerNotFoundException.java, DatabaseFailureException.java
```

## Prerequisites

- JDK 21+ (Java 25 works fine)
- Maven 3.8+

## Build & Run

```bash
mvn compile
mvn exec:java
```

## Run Tests

```bash
mvn test
```

## Output Files

After running:
- `error-log.txt` — database failure stack traces (developer log)
- `repair-order-log.txt` — all repair order updates (audit log)
