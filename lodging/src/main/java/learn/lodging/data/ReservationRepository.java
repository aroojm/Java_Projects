package learn.lodging.data;

import learn.lodging.models.Host;
import learn.lodging.models.Reservation;
import java.util.List;

public interface ReservationRepository {
     List<Reservation> findByHost(Host host);
     Reservation add(Reservation reservation) throws DataException;
     boolean update(Reservation reservation) throws DataException;
     boolean delete(Reservation reservation) throws DataException;
}
