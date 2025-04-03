package account.infrastructure;

import account.domain.ErrorMessage;
import account.infrastructure.CustomExceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalTime;
import java.util.Arrays;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        String message = Arrays.stream(e.getDetailMessageArguments()).toList().toString().substring(3, 22);

        ErrorMessage errorMessage = messageCreator(400, path, message, "Bad Request");

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handlerIllegalStateException(Exception e) {
        return ResponseEntity.status(405).build();
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorMessage> notValidFieldException(HandlerMethodValidationException e, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        String message = Arrays.stream(e.getDetailMessageArguments()).toList().toString();

        ErrorMessage errorMessage = messageCreator(400, path, message, "Bad Request");

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ShortPasswordException.class, BreachedPasswordException.class,
            UserExistException.class, UsedPasswordException.class, ConstraintViolationException.class,
            WrongDateFormatException.class})
    public ResponseEntity<?> incorrectPasswordException(Exception e, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        String message = e.getMessage();

        ErrorMessage errorMessage = messageCreator(400, path, message, "Bad Request");

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(JdbcSQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> existedPaymentRecord(Exception e, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        String message = "This payment already exist";

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
