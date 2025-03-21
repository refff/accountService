package account.presentation;

import account.domain.ErrorMessage;
import account.infrastructure.CustomExceptions.BreachedPasswordException;
import account.infrastructure.CustomExceptions.ShortPasswordException;
import account.infrastructure.CustomExceptions.UsedPasswordException;
import account.infrastructure.CustomExceptions.UserExistException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalTime;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleArgumentNotValidException(Exception e) {
        return ResponseEntity.status(400).build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handlerIllegalStateException(Exception e) {
        return ResponseEntity.status(405).build();
    }

    /*@ExceptionHandler(UserExistException.class)
    public ResponseEntity<ErrorMessage> userExistException(UserExistException e, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(
                LocalTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "User exist!",
                request.getDescription(false).substring(4));

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }*/

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> validationException(Exception e, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        String message = "Bad Request";

        ErrorMessage errorMessage = messageCreator(400, path, message, "Bad Request");

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ShortPasswordException.class, BreachedPasswordException.class, UserExistException.class, UsedPasswordException.class})
    public ResponseEntity<ErrorMessage> incorrectPasswordException(Exception e, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        String message = e.getMessage();

        ErrorMessage errorMessage = messageCreator(400, path, message, "Bad Request");

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    private ErrorMessage messageCreator(int code, String path, String message, String error) {
        ErrorMessage errorMessage = new ErrorMessage();

        errorMessage.setStatus(code);
        errorMessage.setTimestamp(LocalTime.now().toString());
        errorMessage.setPath(path);
        errorMessage.setMessage(message);
        errorMessage.setError(error);

        return errorMessage;

    }
}
