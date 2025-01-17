package ca.jdelreyes.moviereservationsystem.exception.handler;

import ca.jdelreyes.moviereservationsystem.exception.BadRequestException;
import ca.jdelreyes.moviereservationsystem.exception.ConflictException;
import ca.jdelreyes.moviereservationsystem.exception.ForbiddenException;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFoundException(NotFoundException notFoundException) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("message", notFoundException.getMessage());
        return errorMap;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBadRequestException(BadRequestException badRequestException) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("message", badRequestException.getMessage());
        return errorMap;
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> handleForbiddenException(ForbiddenException forbiddenException) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("message", forbiddenException.getMessage());
        return errorMap;
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleConflictException(ConflictException conflictException) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("message", conflictException.getMessage());
        return errorMap;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException) throws IOException {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("message", "Request body is unreadable");
        return errorMap;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("message", methodArgumentNotValidException.getBody().getDetail());
        return errorMap;
    }

    @ExceptionHandler(StripeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleStripeException(StripeException stripeException) {
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("message", stripeException.getMessage());
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("error", errorData);
        return responseData;
    }
}
