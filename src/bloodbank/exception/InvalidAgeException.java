package bloodbank.exception;

/**
 * InvalidAgeException — custom checked exception.
 * Thrown when a donor's age fails validation (e.g., negative or > 120).
 */
public class InvalidAgeException extends Exception {

    public InvalidAgeException(String message) {
        super(message);
    }
}