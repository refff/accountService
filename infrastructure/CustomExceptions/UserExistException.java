package account.infrastructure.CustomExceptions;

public class UserExistException extends RuntimeException {
    public UserExistException() {
        super("User exist!");
    }
}
