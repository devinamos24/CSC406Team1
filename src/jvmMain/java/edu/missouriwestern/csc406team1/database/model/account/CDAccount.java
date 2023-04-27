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

    /**
     * Constructs a new CDAccount object with the specified parameters.
     *
     * @param accountNumber The account number of the CD account.
     * @param customerSSN   The customer's Social Security number associated with the account.
     * @param balance       The initial balance of the account.
     * @param dateOpened    The date the account was opened.
     * @param isActive      The active status of the account.
     * @param interestRate  The interest rate associated with the account.
     * @param dueDate       The due date of the CD.
     */
    public CDAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance,
                     @NotNull LocalDate dateOpened, @NotNull Boolean isActive, @NotNull Double interestRate,
                     @NotNull LocalDate dueDate) {
        super(accountNumber, customerSSN, balance, dateOpened, isActive, interestRate);
        this.dueDate = dueDate;
    }

    /**
     * Creates a copy of the given CDAccount object.
     *
     * @param account The CDAccount object to be copied.
     */
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
        returnValue.add(String.valueOf(getIsActive()));
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
