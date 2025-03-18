package z01dakar.lets_play.exception;

public class NotOwnResourceException extends RuntimeException {
    public NotOwnResourceException(String message) {
        super(message);
    }
}
