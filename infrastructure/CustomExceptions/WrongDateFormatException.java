package account.infrastructure.CustomExceptions;

public class WrongDateFormatException extends RuntimeException {
    public WrongDateFormatException() {
        super("Wrong date format");
    }
}
