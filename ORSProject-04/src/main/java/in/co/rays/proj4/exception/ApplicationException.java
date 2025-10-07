package in.co.rays.proj4.exception;

/**
 * @author Aniket Rajput
 *
 * ApplicationException is a custom exception class used to handle
 * generic application-level exceptions.
 */
public class ApplicationException extends Exception {

    /**
     * Constructs a new ApplicationException with the specified detail message.
     *
     * @param message the detail message
     */
    public ApplicationException(String message) {
        super(message);
    }
}