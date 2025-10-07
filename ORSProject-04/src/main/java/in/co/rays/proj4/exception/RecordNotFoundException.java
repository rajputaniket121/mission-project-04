package in.co.rays.proj4.exception;

/**
 * @author Aniket Rajput
 *
 * RecordNotFoundException is a custom exception class used to indicate
 * that a requested record could not be found in the database.
 */
public class RecordNotFoundException extends Exception {

    /**
     * Constructs a new RecordNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public RecordNotFoundException(String message) {
        super(message);
    }
}