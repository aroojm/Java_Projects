package learn.lodging.models;


import java.util.Objects;

public class Guest {
    private int guestId;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String state;

    public Guest() {
    }

    public Guest(int guestId, String firstName, String lastName, String email, String phone, String state) {
        this.guestId = guestId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.state = state;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return guestId == guest.guestId && firstName.equals(guest.firstName) && lastName.equals(guest.lastName) && email.equals(guest.email) && phone.equals(guest.phone) && state.equals(guest.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guestId, firstName, lastName, email, phone, state);
    }
}
