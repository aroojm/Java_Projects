package learn.lodging.data;

import learn.lodging.models.Guest;
import learn.lodging.models.Host;
import learn.lodging.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationFileRepository implements ReservationRepository {

    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String directory;

    public ReservationFileRepository(@Value("${reservationDataFilePath}") String directory) {
        this.directory = directory;
    }

    @Override
    public List<Reservation> findByHost (Host host) {
        String hostId = host.getHostId();

        ArrayList<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(hostId)))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    result.add(deserialize(fields, hostId));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        List<Reservation> reservations = findByHost(reservation.getHost());
        reservation.setReservationId(getNextId(reservations));
        reservations.add(reservation);
        writeAll(reservations);
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        List<Reservation> all = findByHost(reservation.getHost());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getReservationId() == reservation.getReservationId()) {
                all.set(i, reservation);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Reservation reservation) throws DataException {
        List<Reservation> all = findByHost(reservation.getHost());
        int index = -1;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getReservationId() == reservation.getReservationId()) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            all.remove(index);
            writeAll(all);
            return true;
        }
        return false;
    }

    private String getFilePath(String hostId) {
        return Paths.get(directory, hostId + ".csv").toString();
    }

    private void writeAll(List<Reservation> reservations) throws DataException {
        String hostId = reservations.get(0).getHost().getHostId();
        try (PrintWriter writer = new PrintWriter(getFilePath(hostId))) {

            writer.println(HEADER);

            for (Reservation reservation : reservations) {
                writer.println(serialize(reservation));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }
    private String serialize(Reservation reservation) {
        //id,start_date,end_date,guest_id,total
        return String.format("%s,%s,%s,%s,%s",
                reservation.getReservationId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuest().getGuestId(),
                reservation.getTotal());
    }
    private Reservation deserialize(String[] fields, String hostId) {
        Reservation reservation = new Reservation();
        reservation.setReservationId(Integer.parseInt(fields[0]));
        reservation.setStartDate(LocalDate.parse(fields[1]));
        reservation.setEndDate(LocalDate.parse(fields[2]));

        Guest guest = new Guest();
        guest.setGuestId(Integer.parseInt(fields[3]));
        reservation.setGuest(guest);

        reservation.setTotal(new BigDecimal(fields[4]));

        Host host = new Host();
        host.setHostId(hostId);
        reservation.setHost(host);

        return reservation;
    }

    private int getNextId(List<Reservation> reservations) {
        int nextId = 0;
        for (Reservation reservation : reservations) {
            nextId = Math.max(nextId, reservation.getReservationId());
        }
        return nextId + 1;
    }
}
