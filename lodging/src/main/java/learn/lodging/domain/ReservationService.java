package learn.lodging.domain;

import learn.lodging.data.DataException;
import learn.lodging.data.GuestRepository;
import learn.lodging.data.HostRepository;
import learn.lodging.data.ReservationRepository;
import learn.lodging.models.Guest;
import learn.lodging.models.Host;
import learn.lodging.models.Reservation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class ReservationService {
    private final GuestRepository guestRepository;
    private final HostRepository hostRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(GuestRepository guestRepository, HostRepository hostRepository, ReservationRepository reservationRepository) {
        this.guestRepository = guestRepository;
        this.hostRepository = hostRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findByHost(Host host) {
        Map<String, Host> hostMap = hostRepository.findAll().stream().collect(Collectors.toMap(h -> h.getHostId(), h -> h));
        Map<Integer, Guest> guestMap = guestRepository.findAll().stream().collect(Collectors.toMap(g -> g.getGuestId(), g -> g));

        List<Reservation> reservations = reservationRepository.findByHost(host);
        for(Reservation reservation : reservations) {
            reservation.setHost(hostMap.get(reservation.getHost().getHostId()));
            reservation.setGuest(guestMap.get(reservation.getGuest().getGuestId()));
        }
        return reservations;
    }
    public List<Reservation> findAtHostByGuest(Host host, Guest guest) {
        return findByHost(host).stream()
                .filter(r -> r.getGuest().getGuestId() == guest.getGuestId())
                .collect(Collectors.toList());
    }

    public Result<Reservation> add(Reservation reservation) throws DataException {
        Result<Reservation> result = validateBeforeAdding(reservation);
        if (!result.isSuccess()) {
            return result;
        }
        reservation.setTotal(calculateTotal(reservation));
        result.setPayload(reservationRepository.add(reservation));
        return result;
    }
    public Result<Reservation> update(Reservation reservation) throws DataException {
        Result<Reservation> result = validateBeforeUpdating(reservation);
        if (!result.isSuccess()) {
            return result;
        }
        reservation.setTotal(calculateTotal(reservation));
        reservationRepository.update(reservation);
        result.setPayload(reservation);
        return result;
    }
    public Result<Reservation> delete(Reservation reservation) throws DataException {
        Result<Reservation> result = validateBeforeDeleting(reservation);
        if (!result.isSuccess()) {
            return result;
        }
        reservationRepository.delete(reservation);
        result.setPayload(reservation);
        return result;
    }

    public Result<Reservation> validateBeforeAdding(Reservation reservation) {
        Result<Reservation> result = validateNulls(reservation);
        if (!result.isSuccess()) {
            return result;
        }
        validateDates(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }
        validateChildrenExist(reservation, result);
        return result;
    }
    public Result<Reservation> validateBeforeUpdating(Reservation reservation) {
        Result<Reservation> result = validateNulls(reservation);
        if (!result.isSuccess()) {
            return result;
        }
        validateReservationExists(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }
        validateSameDatesUpdate(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }
        validateDates(reservation, result);

        return result;
    }
    public Result<Reservation> validateBeforeDeleting(Reservation reservation) {
        Result<Reservation> result = validateNulls(reservation);
        if (!result.isSuccess()) {
            return result;
        }
        validateReservationExists(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }
        if (reservation.getStartDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Reservation in past cannot be deleted.");
        }
        return result;
    }

    private Result<Reservation> validateNulls(Reservation reservation) {
        Result<Reservation> result = new Result<>();
        if (reservation == null) {
            result.addErrorMessage("Nothing to save.");
            return result;
        }

        if (reservation.getHost() == null) {
            result.addErrorMessage("Host is required.");
        }

        if (reservation.getGuest() == null) {
            result.addErrorMessage("Guest is required.");
        }

        if (reservation.getStartDate() == null) {
            result.addErrorMessage("Start Date is required.");
        }

        if (reservation.getEndDate() == null) {
            result.addErrorMessage("End Date is required.");
        }
        return result;
    }
    private void validateDates(Reservation reservation, Result<Reservation> result) {
        if (reservation.getStartDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Start date cannot be in the past.");
        }
        if (reservation.getStartDate().isAfter(reservation.getEndDate()) || reservation.getStartDate().equals(reservation.getEndDate())) {
            result.addErrorMessage("Start date must come before the end date.");
        }
        List<Reservation> reservations = findByHost(reservation.getHost());
        // for updating, remove the current reservation from check of overlapping dates
        reservations = reservations.stream().filter(r -> r.getReservationId() != reservation.getReservationId()).collect(Collectors.toList());
        for (Reservation r : reservations) {
            if( reservation.getEndDate().isAfter(r.getStartDate()) && reservation.getEndDate().isBefore(r.getEndDate())  ||
                    reservation.getStartDate().isAfter(r.getStartDate()) && reservation.getStartDate().isBefore(r.getEndDate()) ||
                    r.getEndDate().isAfter(reservation.getStartDate()) && r.getEndDate().isBefore(reservation.getEndDate())  ||
                    r.getStartDate().isAfter(reservation.getStartDate()) && r.getStartDate().isBefore(reservation.getEndDate()) ||
                    r.getStartDate().equals(reservation.getStartDate()) && r.getEndDate().equals(reservation.getEndDate())
            ) {
            result.addErrorMessage("Dates cannot overlap existing reservation dates.");
            return;
            }
        }
    }
    private void validateSameDatesUpdate (Reservation reservation, Result<Reservation> result) {
        List<Reservation> reservations = findByHost(reservation.getHost());
        for (Reservation r : reservations) {
            if( r.getReservationId() == reservation.getReservationId() &&
                    r.getStartDate().equals(reservation.getStartDate()) &&
                    r.getEndDate().equals(reservation.getEndDate())
            ) {
                result.addErrorMessage("No update: Both new dates are same as old dates.");
            }
        }
    }
    private void validateChildrenExist(Reservation reservation, Result<Reservation> result) {
        if (hostRepository.findById(reservation.getHost().getHostId()) == null ||
                reservation.getHost().getHostId() == null) {
            result.addErrorMessage("Host does not exist.");
        }
        if (guestRepository.findById(reservation.getGuest().getGuestId()) == null) {
            result.addErrorMessage("Guest does not exist.");
        }
    }
    private  void validateReservationExists(Reservation reservation, Result<Reservation> result) {
        Reservation existing = findAtHostByGuest(reservation.getHost(), reservation.getGuest()).stream()
                .filter(r -> r.getReservationId() ==  reservation.getReservationId())
                .findFirst().orElse(null);
        if (existing == null) {
            result.addErrorMessage(String.format("Reservation Id %d with Host %s not found.", reservation.getReservationId(), reservation.getHost().getLastName()));
        }
    }

    /**
     * calculates the total bill for stay
     * @return
     */
    public BigDecimal calculateTotal(Reservation reservation) {
        BigDecimal total = BigDecimal.ZERO;
        Host host = reservation.getHost();

        for (LocalDate date = reservation.getStartDate(); date.compareTo(reservation.getEndDate()) < 0 ; date = date.plusDays(1)) {

            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                total = total.add(host.getWeekendRate());
            } else {
                total = total.add(host.getStandardRate());
            }
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

}
