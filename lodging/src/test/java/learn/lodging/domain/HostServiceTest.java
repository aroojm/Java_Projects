package learn.lodging.domain;

import learn.lodging.data.HostRepositoryDouble;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {
    HostService service = new HostService(new HostRepositoryDouble());

    @Test
    void shouldFindByLastName() {
        assertEquals(3, service.findByLastName("H").size());
    }
}