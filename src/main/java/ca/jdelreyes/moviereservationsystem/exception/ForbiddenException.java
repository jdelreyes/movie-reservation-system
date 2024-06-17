package ca.jdelreyes.moviereservationsystem.exception;

public class ForbiddenException extends Exception {
    public ForbiddenException() {
        super("Forbidden");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
