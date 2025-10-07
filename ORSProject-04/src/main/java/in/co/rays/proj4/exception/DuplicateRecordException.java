package in.co.rays.proj4.exception;

/**
 * @author Aniket Rajput
 *
 * DuplicateRecordException is a custom exception class used to indicate
 * an attempt to insert a record that already exists in the database.
 */
public class DuplicateRecordException extends Exception {

    /**
     * Constructs a new DuplicateRecordException with the specified detail message.
     *
     * @param message the detail message
     */
    public DuplicateRecordException(String message) {
        super(message);
    }
}