package edu.missouriwestern.csc406team1.database.model;

import edu.missouriwestern.csc406team1.util.CSV;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

// Check.java
public class Check extends Transaction implements Comparable<Transaction>, CSV {
    private int checkNumber;
    private String payee;
    private Boolean stopPayment;

    public Check(@NotNull String transactionID, @NotNull Boolean credit, @NotNull Boolean debit, @NotNull String transactionType,
                 @NotNull Double amount, @NotNull Double newTotal, @NotNull String accID, @NotNull LocalDate date,
                 @NotNull LocalTime time, int checkNumber, String payee, Boolean stopPayment) {
        super(transactionID, credit, debit, transactionType, amount, newTotal, accID, date, time);
        this.checkNumber = checkNumber;
        this.payee = payee;
        this.stopPayment = stopPayment;
    }

    private Check(Check check) {
        super(check.getTransactionID(), check.isCredit(), check.isDebit(), check.getTransactionType(), check.getAmount(), check.getNewTotal(), check.getAccID(), check.getDate(), check.getTime());
        this.checkNumber = check.getCheckNumber();
        this.payee = check.getPayee();
        this.stopPayment = check.stopPayment;
    }

    // Getters
    public int getCheckNumber() {
        return checkNumber;
    }

    public String getPayee() {
        return payee;
    }

    public Boolean isStopPayment() {
        return stopPayment;
    }

    // Setters
    public void setCheckNumber(int checkNumber) {
        this.checkNumber = checkNumber;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public void setStopPayment(boolean stopPayment) {
        this.stopPayment = stopPayment;
    }

    @Override
    public Check copy() {
        return new Check(this);
    }

    @Override
    public String[] convertToCSV() {
        String[] base = super.convertToCSV();
        ArrayList<String> returnValue = new ArrayList<>(Arrays.asList(base));
        returnValue.add("null");//merchant, not in check
        returnValue.add("null");//Category, not in check
        returnValue.add("null");//posted, not in check
        returnValue.add(String.valueOf(getCheckNumber()));
        returnValue.add(String.valueOf(getPayee()));
        returnValue.add(String.valueOf(isStopPayment()));
        return returnValue.toArray(new String[returnValue.size()]);
    }
}
