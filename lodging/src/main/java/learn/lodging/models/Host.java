package learn.lodging.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Host {
    private String hostId;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private BigDecimal standardRate;
    private BigDecimal weekendRate;

  public Host() {
  }

  public Host(String hostId, String lastName, String email, String phone,
              String address, String city, String state, String postalCode,
              BigDecimal standardRate, BigDecimal weekendRate) {
    this.hostId = hostId;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.address = address;
    this.city = city;
    this.state = state;
    this.postalCode = postalCode;
    this.standardRate = standardRate;
    this.weekendRate = weekendRate;
  }

  public String getHostId() {
    return hostId;
  }

  public void setHostId(String hostId) {
    this.hostId = hostId;
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public BigDecimal getStandardRate() {
    return standardRate;
  }

  public void setStandardRate(BigDecimal standardRate) {
    this.standardRate = standardRate;
  }

  public BigDecimal getWeekendRate() {
    return weekendRate;
  }

  public void setWeekendRate(BigDecimal weekendRate) {
    this.weekendRate = weekendRate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Host host = (Host) o;
    return hostId.equals(host.hostId) && lastName.equals(host.lastName) && email.equals(host.email) && phone.equals(host.phone) && address.equals(host.address) && city.equals(host.city) && state.equals(host.state) && postalCode.equals(host.postalCode) && standardRate.equals(host.standardRate) && weekendRate.equals(host.weekendRate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hostId, lastName, email, phone, address, city, state, postalCode, standardRate, weekendRate);
  }
}
