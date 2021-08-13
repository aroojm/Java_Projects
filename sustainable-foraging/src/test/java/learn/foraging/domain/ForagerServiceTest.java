package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForagerRepositoryDouble;
import learn.foraging.models.Forager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForagerServiceTest {
    ForagerService service = new ForagerService(new ForagerRepositoryDouble());

    @Test
    void shouldAddForager() throws DataException {
        Forager forager = new Forager();
        forager.setFirstName("Test First");
        forager.setLastName("Test Last");
        forager.setState("CA");
        forager.setId("90c70fff-97b5-49d2-ae2f-e7af9993e94b");
        Result<Forager> result = service.add(forager);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals("CA", result.getPayload().getState());
    }

    @Test
    void shouldNotAddDuplicateForager() throws DataException {
        // creating duplicate forager already present in ForagerRepositoryDouble
        Forager forager = new Forager();
        forager.setId("0e4707f4-407e-4ec9-9665-baca0aabe88c");
        forager.setFirstName("Jilly");
        forager.setLastName("Sisse");
        forager.setState("GA");

        Result<Forager> result = service.add(forager);
        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
    }

}