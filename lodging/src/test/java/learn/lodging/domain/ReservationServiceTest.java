package learn.lodging.domain;

import learn.lodging.data.DataException;
import learn.lodging.data.GuestRepositoryDouble;
import learn.lodging.data.HostRepositoryDouble;
import learn.lodging.data.ReservationRepositoryDouble;
import learn.lodging.models.Guest;
import learn.lodging.models.Host;
import learn.lodging.models.Reservation;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService service = new ReservationService(new GuestRepositoryDouble(), new HostRepositoryDouble(), new ReservationRepositoryDouble());

    @Test
    void shouldFindByHost() {
        List<Reservation> actual = service.findByHost(HostRepositoryDouble.HOST2);
        List<Reservation> expected = new ArrayList<>();
        expected.add(ReservationRepositoryDouble.RESERVATION2);
        assertEquals(expected.size(), actual.size());
        assertArrayEquals(expected.toArray(), service.findByHost(HostRepositoryDouble.HOST2).toArray());
    }
    @Test
    void shouldFindAtHostByGuest() {
        List<Reservation> actual = service.findAtHostByGuest(HostRepositoryDouble.HOST1, GuestRepositoryDouble.GUEST1);
        List<Reservation> expected = new ArrayList<>();
        expected.add(ReservationRepositoryDouble.RESERVATION1);
        expected.add(ReservationRepositoryDouble.RESERVATION4);
        assertEquals(expected.size(), actual.size());
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void shouldAddWithCorrectTotalValue() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(2);
        reservation.setStartDate(LocalDate.of(2021,9,10));
        reservation.setEndDate(LocalDate.of(2021,9,20 ));
        reservation.setGuest(GuestRepositoryDouble.GUEST3);
        reservation.setHost(HostRepositoryDouble.HOST3);

        Result<Reservation> result = service.add(reservation);
        assertTrue(result.isSuccess());
        assertEquals(new BigDecimal(3400).setScale(2), result.getPayload().getTotal());
    }

    @Test
    void shouldNotAddNullReservation() throws DataException {
        assertFalse(service.add(null).isSuccess());
        Reservation reservation = new Reservation();
        assertFalse(service.add(reservation).isSuccess());
    }

    @Test
    void shouldNotAddWhenHostNotFound() throws DataException {
        Host host = new Host("hhhh-9999-xxxx-9999","HostTest","testhost@sfgate.com", "(123) 4567890",
                "1 Test Drive","TestCity","WI","98765",new BigDecimal(100),new BigDecimal(100)) ;
        Reservation reservation = new Reservation(1, LocalDate.of(2021,9,10), LocalDate.of(2021,9,20 ),
                GuestRepositoryDouble.GUEST1, host, new BigDecimal(100));
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWhenGuestNotFound() throws DataException {
        Guest guest =  new Guest(1234,"TestGuest","Test","testguest@mediafire.com","(702) 7768761","NV");
        Reservation reservation = new Reservation(1, LocalDate.of(2021,9,10), LocalDate.of(2021,9,20 ),
                guest, HostRepositoryDouble.HOST1, new BigDecimal(100));
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddInvalidDates() throws DataException {
        // start date cannot be in past
        Reservation reservation = new Reservation(999, LocalDate.of(2021,1,1), LocalDate.of(2021,1,10 ),
                GuestRepositoryDouble.GUEST1, HostRepositoryDouble.HOST1, new BigDecimal(100));
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());

        // start date cannot be after end date
         reservation = new Reservation(999, LocalDate.of(2021,1,10), LocalDate.of(2021,1,1 ),
                GuestRepositoryDouble.GUEST1, HostRepositoryDouble.HOST1, new BigDecimal(100));
         result = service.add(reservation);
        assertFalse(result.isSuccess());

        // start & end dates matching an existing reservation
         reservation = new Reservation(999, LocalDate.of(2021,9,1), LocalDate.of(2021,9,10 ),
                GuestRepositoryDouble.GUEST1, HostRepositoryDouble.HOST1, new BigDecimal(100));
        result = service.add(reservation);
        assertFalse(result.isSuccess());

        // overlapping dates with existing reservations
        reservation = new Reservation(999, LocalDate.of(2021,9,5), LocalDate.of(2021,9,15 ),
                GuestRepositoryDouble.GUEST2, HostRepositoryDouble.HOST2, new BigDecimal(100));
        result = service.add(reservation);
        assertFalse(result.isSuccess());

        reservation = new Reservation(999, LocalDate.of(2021,8,30), LocalDate.of(2021,9,5 ),
                GuestRepositoryDouble.GUEST2, HostRepositoryDouble.HOST2, new BigDecimal(100));
        result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldUpdateWithCorrectTotalValue() throws DataException {
        Reservation reservation = new Reservation(1, LocalDate.of(2021,9,5), LocalDate.of(2021,9,11 ),
                GuestRepositoryDouble.GUEST3, HostRepositoryDouble.HOST3, new BigDecimal(100));

        Result<Reservation> result = service.update(reservation);
        assertTrue(result.isSuccess());
        assertEquals(new BigDecimal(1900).setScale(2), result.getPayload().getTotal());
    }

    @Test
    void shouldNotUpdateReservationWithNullFields() throws DataException {
        Reservation reservation = new Reservation();
        assertFalse(service.update(reservation).isSuccess());
    }

    @Test
    void shouldNotUpdateNonExistingReservation() throws DataException {
        Reservation reservation = new Reservation(10, LocalDate.of(2021,12,1), LocalDate.of(2021,12,10 ),
                GuestRepositoryDouble.GUEST3, HostRepositoryDouble.HOST3, new BigDecimal(100));

        Result<Reservation> result = service.update(reservation);
        assertFalse(result.isSuccess());

    }

    @Test
    void shouldNotUpdateUnchangedReservation() throws DataException {
        Reservation reservation = new Reservation(1, LocalDate.of(2021,9,1), LocalDate.of(2021,9,10 ),
                GuestRepositoryDouble.GUEST3, HostRepositoryDouble.HOST3, new BigDecimal(100));

        Result<Reservation> result = service.update(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldDeleteExistingReservation() throws DataException {
        Reservation reservation = new Reservation(3, LocalDate.of(2021,11,1), LocalDate.of(2021,11,10 ),
                GuestRepositoryDouble.GUEST2, HostRepositoryDouble.HOST1, new BigDecimal(100));
        assertTrue(service.delete(reservation).isSuccess());

    }

    @Test
    void shouldNotDeleteNonExistingReservation() throws DataException {
        Reservation reservation = new Reservation(4, LocalDate.of(2021,9,20), LocalDate.of(2021,10,1 ),
                GuestRepositoryDouble.GUEST3, HostRepositoryDouble.HOST2, new BigDecimal(100));
        assertFalse(service.delete(reservation).isSuccess());
    }

    @Test
    void shouldNotDeleteReservationWithNullFields() throws DataException {
        Reservation reservation = new Reservation();
        assertFalse(service.delete(reservation).isSuccess());
    }

    @Test
    void shouldNotDeleteReservationInPast() throws DataException {
        Reservation reservation = new Reservation(4, LocalDate.of(2021,1,1), LocalDate.of(2021,1,10 ),
                GuestRepositoryDouble.GUEST2, HostRepositoryDouble.HOST1, new BigDecimal(100));
        assertFalse(service.delete(reservation).isSuccess());
    }



}