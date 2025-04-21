package account.infrastructure;


import account.domain.ErrorMessage;
import account.infrastructure.CustomExceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalTime;
import java.util.Arrays;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        String message = Arrays.stream(e.getDetailMessageArguments()).toList().toString();

        ErrorMessage errorMessage = messageCreator(400, path, message, "Bad Request");

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handlerIllegalStateException(Exception e, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        String message = e.getMessage();

        ErrorMessage errorMessage = messageCreator(405, path, message, "Method Not Allowed");

        return new ResponseEntity<>(errorMessage, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({UserNotExistException.class, CustomNotFoundException.class})
    public ResponseEntity<ErrorMessage> userNotFoundException(Exception e, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        String message = e.getMessage();

        ErrorMessage errorMessage = messageCreator(404, path, message, "Not Found");

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ BadCredentialsException.class})
    public ResponseEntity<ErrorMessage> authBadCredException(Exception e, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        String message = "BadCredentialsException";

        ErrorMessage errorMessage = messageCreator(401, path, message, "Unauthorized");

        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorMessage> authException(Exception e, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        String message = "AuthenticationException";

        ErrorMessage errorMessage = messageCreator(401, path, message, "Unauthorized");

        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({LockedException.class})
    public ResponseEntity<ErrorMessage> authLockException(Exception e, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        String message = "User account is locked";

        ErrorMessage errorMessage = messageCreator(401, path, message, "Unauthorized");

        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
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
            WrongDateFormatException.class, AdminRemovalException.class, CustomBadRequestException.class})
    public ResponseEntity<?> incorrectPasswordException(Exception e, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        String message = e.getMessage();

        ErrorMessage errorMessage = messageCreator(400, path, message, "Bad Request");

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({AccessDeniedException.class, AuthorizationServiceException.class})
    public ResponseEntity<?> accessDeniedException(AccessDeniedException e, WebRequest request) {
        String path = request.getDescription(false).substring(4);
        String message = "Access Denied!";

        ErrorMessage errorMessage = messageCreator(403, path, message, "Bad Request");

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_ACCEPTABLE);
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
