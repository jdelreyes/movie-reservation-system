package ca.jdelreyes.moviereservationsystem.exception.handler;

import ca.jdelreyes.moviereservationsystem.exception.BadRequestException;
import ca.jdelreyes.moviereservationsystem.exception.ForbiddenException;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
