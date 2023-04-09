package edu.missouriwestern.csc406team1.database.model.account;

import org.jetbrains.annotations.NotNull;

public class ATMCard {
    @NotNull
    private String pinNumber; //access PIN to use card
    @NotNull
    private String customerSSN; //customer that owns this card
    @NotNull
    private String accountNumber; //account this card is linked to

    public ATMCard(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull String pinNumber) {
        this.accountNumber = accountNumber;
        this.customerSSN = customerSSN;
        this.pinNumber = pinNumber;
    }
    @NotNull
    public String getPinNumber() {
        return pinNumber;
    }
    @NotNull
    public String getCustomerSSN() {
        return customerSSN;
    }
    @NotNull
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setPinNumber(@NotNull String pinNumber) {
        this.pinNumber = pinNumber;
    }
    public void setCustomerSSN(@NotNull String customerSSN) {
        this.customerSSN = customerSSN;
    }
    public void setAccountNumber(@NotNull String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
