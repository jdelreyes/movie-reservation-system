package ca.jdelreyes.moviereservationsystem.exception;

public class BadRequestException extends Exception {
    public BadRequestException() {
        super("Bad Request");
    }

    public BadRequestException(String message) {
        super(message);
    }
}
