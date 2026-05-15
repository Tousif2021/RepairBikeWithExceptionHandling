package se.kth.iv1350.repairelectricbike.exception;

/**
 * Thrown when the database cannot be reached, for example because the
 * database server is not running. This is an unrecoverable program
 * malfunction that must be logged and presented as a generic error to
 * the user.
 *
 * <p>This is an <em>unchecked</em> exception ({@link RuntimeException})
 * because it represents a fault in the program's infrastructure rather
 * than an expected business error. Callers are not expected to recover
 * from it; the exception propagates to the view's top-level handler
 * where it is logged and a generic error message is shown.</p>
 */
public class DatabaseFailureException extends RuntimeException {

    /**
     * Creates a new {@link DatabaseFailureException}.
     *
     * @param message A message describing where the failure occurred.
     * @param cause   The underlying exception that triggered this failure.
     */
    public DatabaseFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
