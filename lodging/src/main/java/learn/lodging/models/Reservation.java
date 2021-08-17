package learn.lodging.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Reservation {

    private int reservationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Guest guest;
    private Host host;
    private BigDecimal total;

    public Reservation() {
    }

    public Reservation(int reservationId, LocalDate startDate, LocalDate endDate, Guest guest, Host host, BigDecimal total) {
        this.reservationId = reservationId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guest = guest;
        this.host = host;
        this.total = total;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId (int reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }


    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return reservationId == that.reservationId && startDate.equals(that.startDate) && endDate.equals(that.endDate) && guest.equals(that.guest) && host.equals(that.host) && total.equals(that.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId, startDate, endDate, guest, host, total);
    }
}
