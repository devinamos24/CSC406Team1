package edu.missouriwestern.csc406team1.database.model.account;

import edu.missouriwestern.csc406team1.util.DateConverter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a certificate of deposit (CD) savings account in the bank system.
 */
public class CDAccount extends SavingsAccount {

    @NotNull
    private LocalDate dueDate;   //Date CD is due to complete

    public CDAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull LocalDate dateOpened, @NotNull Boolean isActive, @NotNull Double interestRate, @NotNull LocalDate dueDate) {
        super(accountNumber, customerSSN, balance, dateOpened, isActive, interestRate);
        this.dueDate = dueDate;
    }

    private CDAccount(CDAccount account) {
        super(account.getAccountNumber(), account.getCustomerSSN(), account.getBalance(), account.getDateOpened(), account.getIsActive(), Objects.requireNonNull(account.getInterestRate()));
        this.dueDate = account.dueDate;
    }

    @NotNull
    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(@NotNull LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String[] convertToCSV() {
        ArrayList<String> returnValue = new ArrayList<>();
        returnValue.add(getAccountNumber());
        returnValue.add(getCustomerSSN());
        returnValue.add(String.valueOf(getBalance()));
        returnValue.add(DateConverter.convertDateToString(getDateOpened()));
        returnValue.add("CD");
        returnValue.add(String.valueOf(getInterestRate()));
        returnValue.add(DateConverter.convertDateToString(getDueDate()));

        return returnValue.toArray(new String[returnValue.size()]);
    }

    @Override
    public CDAccount copy() {
        return new CDAccount(this);
    }
}
