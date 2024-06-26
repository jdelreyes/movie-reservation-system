package ca.jdelreyes.moviereservationsystem.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SeatReservationClientTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void BuyTicketShouldReturnTicketResponseAnd201HttpStatusCode() {

    }
}
