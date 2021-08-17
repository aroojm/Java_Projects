package learn.lodging.domain;

import learn.lodging.data.GuestRepositoryDouble;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {

    GuestService service = new GuestService(new GuestRepositoryDouble());

    @Test
    void shouldFindByLastName() {
        assertEquals(3, service.findByLastName("Test").size());
    }
}