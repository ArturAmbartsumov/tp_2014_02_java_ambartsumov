package exceptions;

/**
 * Created by artur on 30.05.14.
 */
public class ParserException extends Exception {
    public ParserException(Exception cause) {
        super(cause);
    }
}
