package edu.missouriwestern.csc406team1.database;

import edu.missouriwestern.csc406team1.util.CSV;
import org.jetbrains.annotations.NotNull;

/**
 * This class is our model for a customer object
 * It implements comparable and csv to aid in sorting and writing to disk
 */
public class Customer implements Comparable<Customer>, CSV {
    @NotNull
    private final String ssn;
    @NotNull
    private String address;
    @NotNull
    private String city;
    @NotNull
    private String state;
    @NotNull
    private String zipcode;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;

    public Customer(@NotNull String ssn,
                    @NotNull String address,
                    @NotNull String city,
                    @NotNull String state,
                    @NotNull String zipcode,
                    @NotNull String firstname,
                    @NotNull String lastname) {
        this.ssn = ssn;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @NotNull
    public String getSsn() {
        return ssn;
    }

    @NotNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NotNull String address) {
        this.address = address;
    }

    @NotNull
    public String getCity() {
        return city;
    }

    public void setCity(@NotNull String city) {
        this.city = city;
    }

    @NotNull
    public String getState() {
        return state;
    }

    public void setState(@NotNull String state) {
        this.state = state;
    }

    @NotNull
    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(@NotNull String zipcode) {
        this.zipcode = zipcode;
    }

    @NotNull
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(@NotNull String firstname) {
        this.firstname = firstname;
    }

    @NotNull
    public String getLastname() {
        return lastname;
    }

    public void setLastname(@NotNull String lastname) {
        this.lastname = lastname;
    }

    @Override
    public int compareTo(@NotNull Customer o) {
        return this.firstname.compareTo(o.firstname);
    }

    @Override
    public String[] convertToCSV() {
        return new String[] {ssn, address, city, state, zipcode, firstname, lastname};
    }
}
