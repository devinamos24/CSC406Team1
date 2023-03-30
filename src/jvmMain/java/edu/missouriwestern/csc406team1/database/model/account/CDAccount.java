package edu.missouriwestern.csc406team1.database.model.account;

import edu.missouriwestern.csc406team1.util.DateConverter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Represents a certificate of deposit (CD) savings account in the bank system.
 */
public class CDAccount extends SavingsAccount {

    @NotNull
    private Date dueDate;   //Date CD is due to complete

    public CDAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull Date dateOpened, @NotNull Double interestRate, @NotNull Date dueDate) {
        super(accountNumber, customerSSN, balance, dateOpened, interestRate);
        this.dueDate = dueDate;
    }

    @NotNull
    public Date getDueDate() { return dueDate; }
    public void setDueDate(@NotNull Date dueDate) { this.dueDate = dueDate; }

    @Override
    public String[] convertToCSV() {
        String[] base = super.convertToCSV();
        ArrayList<String> returnValue = new ArrayList<>(Arrays.asList(base));
        returnValue.add(String.valueOf(getInterestRate()));
        returnValue.add(DateConverter.convertDateToString(getDueDate()));

        return (String[]) returnValue.toArray();
    }
}
