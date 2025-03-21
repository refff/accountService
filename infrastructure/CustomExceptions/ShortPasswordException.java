package account.infrastructure.CustomExceptions;

public class ShortPasswordException extends RuntimeException {
    public ShortPasswordException() {
        super("Password length must be 12 chars minimum!");
    }
}
