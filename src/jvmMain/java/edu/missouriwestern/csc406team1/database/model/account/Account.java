package edu.missouriwestern.csc406team1.database.model.account;

import edu.missouriwestern.csc406team1.util.CSV;
import edu.missouriwestern.csc406team1.util.Copyable;
import edu.missouriwestern.csc406team1.util.DateConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

public abstract class Account implements CSV, Comparable<Account>, Copyable<Account> {
    @NotNull
    private String accountNumber;//account number to reference the account
    @NotNull
    private String customerSSN;// SSN of customer owning the account
    @NotNull
    private Double balance;//current balance of the account
    @NotNull
    private LocalDate dateOpened;//date the account was opened with the bank
    @NotNull
    private Boolean isActive;
    @Nullable // The interest rate can be null if it is a basic checking account
    private Double interestRate;//current interest rate of the account

    public Account(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull LocalDate dateOpened, @NotNull Boolean isActive, @Nullable Double interestRate) {
        this.accountNumber = accountNumber;
        this.customerSSN = customerSSN;
        this.balance = balance;
        this.dateOpened = dateOpened;
        this.isActive = isActive;
        this.interestRate = interestRate;
    }

    public int compareTo(Account o) {
        return this.dateOpened.compareTo(o.dateOpened) * -1;
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
    public LocalDate getDateOpened() {
        return dateOpened;
    }
    public void setDateOpened(@NotNull LocalDate dateOpened) {
        this.dateOpened = dateOpened;
    }

    @NotNull
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(@NotNull Boolean isActive) {
        this.isActive = isActive;
    }

    @Nullable
    public Double getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(@Nullable Double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public String[] convertToCSV() {
        return new String[] {accountNumber, customerSSN, String.valueOf(balance), DateConverter.convertDateToString(dateOpened), String.valueOf(isActive)};
    }
}
