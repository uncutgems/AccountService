package account.exception_handling;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorHandler {
    @ExceptionHandler({ConstraintViolationException.class})
    public void handleConstraintViolationException(ConstraintViolationException exception,
                                                   ServletWebRequest webRequest) throws IOException {
        assert webRequest.getResponse() != null;
        String message = exception.getMessage().split(": ")[1];
        webRequest.getResponse().sendError(HttpStatus.BAD_REQUEST.value(), message);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                      ServletWebRequest webRequest) throws IOException {
        assert webRequest.getResponse() != null;
        String message = exception.getBindingResult().getAllErrors().stream().
                map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()).get(0);
        System.out.println(message);
        webRequest.getResponse().sendError(HttpStatus.BAD_REQUEST.value(), "Password length must be 12 chars minimum!");

    }
}
