package edu.missouriwestern.csc406team1.database.model;

import edu.missouriwestern.csc406team1.util.CSV;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

// Check.java
public class Check extends Transaction implements Comparable<Transaction>, CSV {
    private int checkNumber;
    private String payee;
    private boolean stopPayment;

    public Check(@NotNull String transactionID, @NotNull Boolean credit, @NotNull Boolean debit, @NotNull String transactionType,
                 @NotNull Double amount, @NotNull Double newTotal, @NotNull String accID, @NotNull LocalDate date,
                 @NotNull LocalTime time, int checkNumber, String payee) {
        super(transactionID, credit, debit, transactionType, amount, newTotal, accID, date, time);
        this.checkNumber = checkNumber;
        this.payee = payee;
        this.stopPayment = false;
    }

    // Getters
    public int getCheckNumber() {
        return checkNumber;
    }

    public String getPayee() {
        return payee;
    }

    public boolean isStopPayment() {
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

}
