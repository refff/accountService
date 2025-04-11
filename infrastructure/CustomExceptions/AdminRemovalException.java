package account.infrastructure.CustomExceptions;

public class AdminRemovalException extends RuntimeException {
    public AdminRemovalException() {
        super("Can't remove ADMINISTRATOR role!");
    }
}
