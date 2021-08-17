package learn.lodging.data;

import learn.lodging.models.Guest;
import learn.lodging.models.Host;
import learn.lodging.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository{

    private final ArrayList<Reservation> reservations = new ArrayList<>();

    public final static Reservation RESERVATION1 = new Reservation(1, LocalDate.of(2021,9,1), LocalDate.of(2021,9,10 ),
            GuestRepositoryDouble.GUEST1, HostRepositoryDouble.HOST1, new BigDecimal(100));
    public final static Reservation RESERVATION2 = new Reservation(1, LocalDate.of(2021,9,1), LocalDate.of(2021,9,10 ),
            GuestRepositoryDouble.GUEST2, HostRepositoryDouble.HOST2, new BigDecimal(100));
    public final static Reservation RESERVATION3 = new Reservation(1, LocalDate.of(2021,9,1), LocalDate.of(2021,9,10 ),
            GuestRepositoryDouble.GUEST3, HostRepositoryDouble.HOST3, new BigDecimal(100));
    public final static Reservation RESERVATION4 = new Reservation(2, LocalDate.of(2021,10,1), LocalDate.of(2021,10,10 ),
            GuestRepositoryDouble.GUEST1, HostRepositoryDouble.HOST1, new BigDecimal(100));
    public final static Reservation RESERVATION5 = new Reservation(3, LocalDate.of(2021,11,1), LocalDate.of(2021,11,10 ),
            GuestRepositoryDouble.GUEST2, HostRepositoryDouble.HOST1, new BigDecimal(100));
    public final static Reservation RESERVATION6 = new Reservation(4, LocalDate.of(2021,1,1), LocalDate.of(2021,1,10 ),
            GuestRepositoryDouble.GUEST2, HostRepositoryDouble.HOST1, new BigDecimal(100));

    public ReservationRepositoryDouble() {
        reservations.add(RESERVATION1);
        reservations.add(RESERVATION2);
        reservations.add(RESERVATION3);
        reservations.add(RESERVATION4);
        reservations.add(RESERVATION5);
        reservations.add(RESERVATION6);
    }

    @Override
    public List<Reservation> findByHost(Host host) {
        return reservations.stream().filter(r -> r.getHost().equals(host)).collect(Collectors.toList());
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        return true;
    }

    @Override
    public boolean delete(Reservation reservation) throws DataException {
        return true;
    }
}
