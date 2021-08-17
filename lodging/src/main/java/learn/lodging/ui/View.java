package learn.lodging.ui;

import learn.lodging.models.Guest;
import learn.lodging.models.Host;
import learn.lodging.models.Reservation;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class View {
    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }

    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            io.printf("%s. %s%n", option.getValue(), option.getMessage());

            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }

    public Host chooseHost(List<Host> hosts) {
        if (hosts.size() == 0) {
            io.println("No hosts found");
            return null;
        }

        int index = 1;
        for (Host host : hosts.stream().limit(25).collect(Collectors.toList())) {
            io.printf("%s: %s ---- %s%n", index++, host.getLastName(), host.getEmail());
        }
        index--;

        if (hosts.size() > 25) {
            io.println("More than 25 hosts found. Showing first 25. Please refine your search.");
        }
        io.println("0: Exit");
        String message = String.format("Select a host by their index [0-%s]: ", index);

        index = io.readInt(message, 0, index);
        if (index <= 0) {
            return null;
        }
        return hosts.get(index - 1);
    }
    public Guest chooseGuest(List<Guest> guests) {
        if (guests.size() == 0) {
            io.println("No guests found");
            return null;
        }

        int index = 1;
        for (Guest guest : guests.stream().limit(25).collect(Collectors.toList())) {
            io.printf("%s: %s, %s ---- %s%n", index++, guest.getLastName(), guest.getFirstName(), guest.getEmail());
        }
        index--;

        if (guests.size() > 25) {
            io.println("More than 25 guests found. Showing first 25. Please refine your search.");
        }
        io.println("0: Exit");
        String message = String.format("Select a guest by their index [0-%s]: ", index);

        index = io.readInt(message, 0, index);
        if (index <= 0) {
            return null;
        }
        return guests.get(index - 1);
    }
    public Reservation chooseReservation(List<Reservation> reservations) {
        if (reservations.size() == 0) {
            io.println("No reservations found");
            return null;
        }
        printReservationHeader(reservations.get(0).getHost());
        int index = displayReservations(reservations);

        io.println("0: Exit");
        String message = String.format("Select a reservation by its index [0-%s]: ", index);

        index = io.readInt(message, 0, index);
        if (index <= 0) {
            return null;
        }
        return reservations.get(index - 1);
    }

    public Reservation createReservation(Host host, Guest guest) {
        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setStartDate(io.readLocalDate("Start date [MM/dd/yyyy]: ", true));
        reservation.setEndDate(io.readLocalDate("End date [MM/dd/yyyy]: ", true));
        return reservation;
    }
    public Reservation updateReservation(Reservation reservation) {
        displayHeader(String.format("Editing Reservation %s", reservation.getReservationId()));
        io.println("[Press Enter to keep the current date]");

        LocalDate start = reservation.getStartDate();
        LocalDate end = reservation.getEndDate();
        LocalDate temp;

        temp = io.readLocalDate(String.format("Start (%s): ",io.formatDate(start)), false);
        if (temp != LocalDate.EPOCH) { start = temp;}

        temp = io.readLocalDate(String.format("End (%s): ",io.formatDate(end)), false);
        if (temp != LocalDate.EPOCH) { end = temp;}

        reservation.setStartDate(start);
        reservation.setEndDate(end);

        return reservation;
    }
    public boolean confirmReservationSummary(Reservation reservation) {
        displayHeader("Summary");
        String successMessage = String.format("Host Last Name: %s\nGuest Last Name: %s\nStart: %s\nEnd: %s\nTotal: $%.2f\n",
                reservation.getHost().getLastName(),
                reservation.getGuest().getLastName(),
                io.formatDate(reservation.getStartDate()),
                io.formatDate(reservation.getEndDate()),
                reservation.getTotal().doubleValue());
        io.printf(successMessage);
        return io.readBoolean("Is this okay? [y/n]: ");
    }
    public boolean confirmReservationDelete(Reservation reservation) {
        displayHeader("Confirm Delete");
        String successMessage = String.format("Host Last Name: %s\nGuest Last Name: %s\nStart: %s\nEnd: %s\n",
                reservation.getHost().getLastName(),
                reservation.getGuest().getLastName(),
                io.formatDate(reservation.getStartDate()),
                io.formatDate(reservation.getEndDate()));
        io.printf(successMessage);
        return io.readBoolean("Is this okay to delete? [y/n]: ");
    }

    private int displayReservations(List<Reservation> reservations) {
        int index = 1;
        for (Reservation r : reservations) {
            io.printf("%2d: Reservation ID: %3d,   %s - %s,   Guest: %15s,   Email: %s\n",
                    index++,
                    r.getReservationId(),
                    io.formatDate(r.getStartDate()),
                    io.formatDate(r.getEndDate()),
                    String.format("%s,%s", r.getGuest().getLastName(), r.getGuest().getFirstName()),
                    r.getGuest().getEmail());
        }
        index--;
        return index;
    }
    public void displayReservations(List<Reservation> reservations, Guest guest) {
        Host host = reservations.get(0).getHost();
        io.printf("\nGuest Email: %s\n", guest.getEmail());
        printReservationHeader(host);
        displayReservations(reservations);
    }
    public void displayReservationsForHost(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No reservations found");
            return;
        }
        printReservationHeader(reservations.get(0).getHost());
        displayReservations(reservations);
    }

    private void printReservationHeader(Host host) {
        io.printf("\nHost Email: %s\n", host.getEmail());
        displayHeader(String.format("%s: %s, %s",host.getLastName(), host.getCity(), host.getState()));
    }
    public String readLastName(String input) {
        return io.readRequiredString(String.format("%s last name starts with: ", input));
    }
    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }
    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }
    public void displayStatus(String message) {
        io.printf("\n%s\n", message);
    }
    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }
    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }
}
