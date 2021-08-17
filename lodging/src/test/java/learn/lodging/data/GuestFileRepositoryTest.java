package learn.lodging.data;

import learn.lodging.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {

    private static final String SEED_PATH = "./data/guests-seed.csv";
    private static final String TEST_PATH = "./data/guests-test.csv" ;
    private GuestFileRepository repository = new GuestFileRepository(TEST_PATH);

    @BeforeEach
    void setUp() throws IOException {
        Files.copy(Paths.get(SEED_PATH), Paths.get(TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() {
        List<Guest> all = repository.findAll();
        assertEquals(1000, all.size());
    }

    @Test
    void shouldFindById() {
        Guest guest = repository.findById(500);
        assertNotNull(guest);
        assertEquals(500, guest.getGuestId());
    }

    @Test
    void shouldFindByLastName() {

        List<Guest> guests = repository.findByLastName("KO");
        assertNotNull(guests);
        assertEquals(4,guests.size());

        guests = repository.findByLastName("y");
        assertNotNull(guests);
        assertEquals(10,guests.size());
    }
}