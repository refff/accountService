package account.infrastructure.CustomExceptions;

public class UserNotExistException extends RuntimeException {
    public UserNotExistException() {
        super("User not found!");
    }
}
