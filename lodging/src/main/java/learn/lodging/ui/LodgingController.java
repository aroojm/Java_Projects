package learn.lodging.ui;

import learn.lodging.data.DataException;
import learn.lodging.domain.GuestService;
import learn.lodging.domain.HostService;
import learn.lodging.domain.ReservationService;
import learn.lodging.domain.Result;
import learn.lodging.models.Guest;
import learn.lodging.models.Host;
import learn.lodging.models.Reservation;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class LodgingController {
    private final GuestService guestService;
    private final HostService hostService;
    private final ReservationService reservationService;
    private final View view;

    public LodgingController(GuestService guestService, HostService hostService, ReservationService reservationService, View view) {
        this.guestService = guestService;
        this.hostService = hostService;
        this.reservationService = reservationService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Welcome to DON'T WRECK MY HOUSE");
        try {
            runAppLoop();
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }
    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS_FOR_HOST:
                    viewByHost();
                    break;
                case MAKE_RESERVATION:
                    makeReservation();
                    break;
                case  EDIT_RESERVATION:
                    editReservation();
                    break;
                case CANCEL_RESERVATION:
                    cancelReservation();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }

    private void viewByHost() {
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_FOR_HOST.getMessage());
        Host host = getHost();
        if (host == null) {
            return;
        }
        List<Reservation> reservations = reservationService.findByHost(host);
        view.displayReservationsForHost(reservations);
    }
    private void makeReservation() throws DataException {
        view.displayHeader(MainMenuOption.MAKE_RESERVATION.getMessage());
        Guest guest = getGuest();
        if (guest == null) {
            return;
        }
        Host host = getHost();
        if (host == null) {
            return;
        }
        Reservation reservation = view.createReservation(host, guest);
        // total set here for displaying in summary
        reservation.setTotal(reservationService.calculateTotal(reservation));

        // get current reservations with host, in case of displaying errors
        List<Reservation> reservations = reservationService.findByHost(host);
        Result<Reservation> result = reservationService.validateBeforeAdding(reservation);
        if (!result.isSuccess()) {
            view.displayReservations(reservations, guest);
            view.displayStatus(false, result.getErrorMessages());
            return;
        }

        // display & confirm reservation before saving in data files
        if (view.confirmReservationSummary(reservation)) {
            result = reservationService.add(reservation);
            // validateBeforeAdding done above so not checking for errors in result again
            String successMessage = String.format("Reservation %s created with host %s.",
                    result.getPayload().getReservationId(),
                    result.getPayload().getHost().getLastName());
            view.displayStatus(true, successMessage);
        } else {
            String message = String.format("Reservation NOT created.");
            view.displayStatus(message);
        }
    }
    private void editReservation() throws DataException {
        view.displayHeader(MainMenuOption.EDIT_RESERVATION.getMessage());
        Reservation reservation = getReservation();
        if (reservation == null) {
            return;
        }
        reservation = view.updateReservation(reservation);
        // total set here for displaying in summary
        reservation.setTotal(reservationService.calculateTotal(reservation));

        Result<Reservation> result = reservationService.validateBeforeUpdating(reservation);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
            return;
        }

        // display & confirm update before saving in data files
        if (view.confirmReservationSummary(reservation)) {
            result = reservationService.update(reservation);
            // validateBeforeUpdating done above so not checking for errors in result again
            String successMessage = String.format("Reservation %s updated.",
                    result.getPayload().getReservationId());
            view.displayStatus(true, successMessage);
        } else {
            String message = String.format("Reservation %s NOT updated.",
                   reservation.getReservationId());
            view.displayStatus(message);
        }
    }
    private void cancelReservation() throws DataException {
        view.displayHeader(MainMenuOption.CANCEL_RESERVATION.getMessage());
        Reservation reservation = getReservation();
        if (reservation == null) {
            return;
        }

        Result<Reservation> result = reservationService.validateBeforeDeleting(reservation);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
            return;
        }

        // display & confirm delete before saving in data files
        if (view.confirmReservationDelete(reservation)) {
            result = reservationService.delete(reservation);
            // validateBeforeDeleting done above so not checking for errors in result again
            String successMessage = String.format("Reservation %s cancelled.",
                    result.getPayload().getReservationId());
            view.displayStatus(true, successMessage);
        } else {
            String message = String.format("Reservation %s NOT cancelled.",
                    reservation.getReservationId());
            view.displayStatus(message);
        }
    }

    private Host getHost() {
        String lastNamePrefix = view.readLastName("Host");
        List<Host> hosts = hostService.findByLastName(lastNamePrefix);
        return view.chooseHost(hosts);
    }
    private Guest getGuest() {
        String lastNamePrefix = view.readLastName("Guest");
        List<Guest> guests = guestService.findByLastName(lastNamePrefix);
        return view.chooseGuest(guests);

    }
    private Reservation getReservation() {
        Host host = getHost();
        if (host == null) {
            return null;
        }
        List<Reservation> reservations = reservationService.findByHost(host);
        return view.chooseReservation(reservations);
    }
}
