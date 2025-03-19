package ma.sool.system.exception;

import ma.sool.system.Result;
import ma.sool.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler(ObjectNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  Result handleObjectNotFoundException(ObjectNotFoundException ex) {
    return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Result handleValidationException(MethodArgumentNotValidException ex) {
    List<ObjectError> errors = ex.getBindingResult().getAllErrors();
    Map<String, String> map = new HashMap<>(errors.size());
    errors.forEach((error) -> {
      String key = ((FieldError) error).getField();
      String val = error.getDefaultMessage();
      map.put(key, val);
    });
    return new Result(false, StatusCode.INVALID_ARGUMENT,
            "Provided arguments are invalid, see data for details.", map);
  }

  @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  Result handleAuthenticationException(Exception ex) {
    return new Result(false, StatusCode.UNAUTHORIZED,
            "username or password is incorrect", ex.getMessage());
  }
  @ExceptionHandler(AccountStatusException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  Result handleAccountStatusException(AccountStatusException ex) {
    return new Result(false, StatusCode.UNAUTHORIZED,
            "Account status is not valid", ex.getMessage());
  }
  @ExceptionHandler(InvalidBearerTokenException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  Result handleInvalidTokenException(InvalidBearerTokenException ex) {
    return new Result(false, StatusCode.FORBIDDEN,
            "Token provided is expired, revoked, malformed, or invalid etc.", ex.getMessage());
  }
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  Result handleAccessDeniedException(AccessDeniedException ex) {
    return new Result(false, StatusCode.FORBIDDEN,
            "Access is denied.", ex.getMessage());
  }
  @ExceptionHandler(InsufficientAuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  Result handleInsufficientAuthException(InsufficientAuthenticationException ex) {
    return new Result(false, StatusCode.UNAUTHORIZED,
            "Credential is insufficient(ID,Password required).", ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  Result handleException(Exception ex) {
    return new Result(false, StatusCode.INTERNAL_SERVER_ERROR,
            "Server is not working properly.", ex.getMessage());
  }
}
