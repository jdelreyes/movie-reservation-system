package ca.jdelreyes.moviereservationsystem.model.enums;

public enum MovieType {
    REGULAR(13.00),
    MAX(20.00);

    public final Double price;

    private MovieType(Double price) {
        this.price = price;
    }
}
