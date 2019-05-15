package location.exception;

public class InvalidAuthTokenException extends Exception {
    private static final long serialVersionUID = 1L;
    public InvalidAuthTokenException(String s) {
        super(s);
    }
}
