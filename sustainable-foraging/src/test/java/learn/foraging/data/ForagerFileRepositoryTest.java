package learn.foraging.data;

import learn.foraging.models.Forager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ForagerFileRepositoryTest {

    private static final String SEED_PATH = "./data/foragers-seed.txt";
    private static final String TEST_PATH = "./data/foragers-test.txt" ;
    private ForagerFileRepository repository = new ForagerFileRepository(TEST_PATH);

    @BeforeEach
    void setUp() throws IOException {
        Files.copy(Paths.get(SEED_PATH), Paths.get(TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() {
        List<Forager> all = repository.findAll();
        assertEquals(1000, all.size());
    }

    @Test
    void shouldAddForager() throws DataException {
        Forager forager = new Forager();
        forager.setState("WI");
        forager.setFirstName("Test First");
        forager.setLastName("Test Last");

        Forager actual = repository.add(forager);
        assertNotNull(actual);
        assertEquals("WI", actual.getState());
    }
}