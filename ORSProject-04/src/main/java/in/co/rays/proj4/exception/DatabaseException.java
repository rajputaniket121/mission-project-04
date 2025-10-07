package in.co.rays.proj4.exception;

/**
 * @author Aniket Rajput
 *
 * DatabaseException is a custom exception class used to indicate
 * database-related errors.
 */
public class DatabaseException extends Exception {

    /**
     * Constructs a new DatabaseException with the specified detail message.
     *
     * @param message the detail message
     */
    public DatabaseException(String message) {
        super(message);
    }
}