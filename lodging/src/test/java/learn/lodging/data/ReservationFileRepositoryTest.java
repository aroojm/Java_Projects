package learn.lodging.data;

import learn.lodging.models.Guest;
import learn.lodging.models.Host;
import learn.lodging.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {

    static final String SEED_FILE_PATH = "./data/reservations-seed-2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";
    static final String TEST_FILE_PATH = "./data/reservations_test/2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";
    static final String TEST_DIR_PATH = "./data/reservations_test";
    static final int RESERVATIONS_COUNT = 12;

    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIR_PATH);

    @BeforeEach
    void setup() throws IOException {
        Files.copy(Paths.get(SEED_FILE_PATH), Paths.get(TEST_FILE_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindByHost() {
        Host host = new Host();
        host.setHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        List<Reservation> reservations = repository.findByHost(host);
        assertEquals(RESERVATIONS_COUNT, reservations.size());
    }

    @Test
    void shouldAdd() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2021,8,10));
        reservation.setEndDate(LocalDate.of(2021,8,15));

        Guest guest = new Guest();
        guest.setGuestId(1);
        reservation.setGuest(guest);

        reservation.setTotal(new BigDecimal(100));

        Host host = new Host();
        host.setHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        reservation.setHost(host);

        Reservation expected = repository.add(reservation);

        assertEquals(1, expected.getGuest().getGuestId());
        assertEquals(13, expected.getReservationId());
        assertEquals(new BigDecimal(100), expected.getTotal());
    }

    @Test
    void shouldUpdate() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setStartDate(LocalDate.of(2021,12,1));
        reservation.setEndDate(LocalDate.of(2021,12,31));

        Guest guest = new Guest();
        guest.setGuestId(663);
        reservation.setGuest(guest);

        reservation.setTotal(new BigDecimal(400));

        Host host = new Host();
        host.setHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        reservation.setHost(host);

        assertTrue(repository.update(reservation));
    }

    @Test
    void shouldDelete() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(12);

        Guest guest = new Guest();
        guest.setGuestId(735);
        reservation.setGuest(guest);


        Host host = new Host();
        host.setHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        reservation.setHost(host);

        assertTrue(repository.delete(reservation));
    }


}