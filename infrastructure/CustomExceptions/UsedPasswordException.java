package account.infrastructure.CustomExceptions;

public class UsedPasswordException extends RuntimeException {
    public UsedPasswordException() {
        super("The passwords must be different!");
    }
}
