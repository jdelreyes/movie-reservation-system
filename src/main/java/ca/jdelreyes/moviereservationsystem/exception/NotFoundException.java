package ca.jdelreyes.moviereservationsystem.exception;

public class NotFoundException extends Exception {
    public NotFoundException() {
        super("Not Found");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
