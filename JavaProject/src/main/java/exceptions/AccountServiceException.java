package exceptions;

/**
 * Created by artur on 29.03.14.
 */
public class AccountServiceException extends Exception {
    public AccountServiceException(String message) {
        super(message);
    }
}
