package edu.missouriwestern.csc406team1.database.model.account;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public abstract class Account {
    @NotNull
    private String accountNumber;//account number to reference the account
    @NotNull String customerSSN;// SSN of customer owning the account
    @NotNull
    private Double balance;//current balance of the account
    @NotNull
    private Date dateOpened;//date the account was opened with the bank
    @Nullable // The interest rate can be null if it is a basic checking account
    private Double interestRate;//current interest rate of the account
    public Account(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull Date dateOpened, @Nullable Double interestRate) {
        this.accountNumber = accountNumber;
        this.customerSSN = customerSSN;
        this.balance = balance;
        this.dateOpened = dateOpened;
        this.interestRate = interestRate;
    }

    @NotNull
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(@NotNull String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @NotNull
    public String getCustomerSSN() {
        return customerSSN;
    }

    public void setCustomerSSN(@NotNull String customerSSN) {
        this.customerSSN = customerSSN;
    }

    @NotNull
    public Double getBalance() {
        return balance;
    }
    public void setBalance(@NotNull Double balance) {
        this.balance = balance;
    }

    @NotNull
    public Date getDateOpened() {
        return dateOpened;
    }
    public void setDateOpened(@NotNull Date dateOpened) {
        this.dateOpened = dateOpened;
    }

    @Nullable
    public Double getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(@Nullable Double interestRate) {
        this.interestRate = interestRate;
    }
}
