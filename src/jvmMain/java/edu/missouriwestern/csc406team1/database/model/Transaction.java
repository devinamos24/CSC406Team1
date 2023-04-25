package edu.missouriwestern.csc406team1.database.model;

import edu.missouriwestern.csc406team1.util.CSV;
import edu.missouriwestern.csc406team1.util.Copyable;
import edu.missouriwestern.csc406team1.util.DateConverter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction implements Comparable<Transaction>, CSV, Copyable<Transaction> {
    @NotNull
    private String transactionID;
    @NotNull
    private Boolean credit;
    @NotNull
    private Boolean debit;
    @NotNull
    private Double amount;
    @NotNull
    private String accID;
    @NotNull
    private Double newTotal;
    @NotNull
    private LocalDate date;
    @NotNull
    private LocalTime time;
    @NotNull
    private String transactionType;
    // Constructor
    public Transaction(@NotNull String transactionID, @NotNull Boolean credit, @NotNull Boolean debit, @NotNull String transactionType,
                       @NotNull Double amount, @NotNull Double newTotal, @NotNull String accID, @NotNull LocalDate date,
                       @NotNull LocalTime time) {
        this.transactionID = transactionID;
        this.credit = credit;
        this.debit = debit;
        this.transactionType = transactionType;
        this.amount = amount;
        this.newTotal = newTotal;
        this.accID = accID;
        this.date = date;
        this.time = time;
    }

    private Transaction(Transaction transaction) {
        this.transactionID = transaction.transactionID;
        this.credit = transaction.credit;
        this.debit = transaction.debit;
        this.transactionType = transaction.transactionType;
        this.amount = transaction.amount;
        this.newTotal = transaction.newTotal;
        this.accID = transaction.accID;
        this.date = transaction.date;
        this.time = transaction.time;
    }

    // Getters and Setters
    @NotNull
    public Boolean isCredit() {
        return credit;
    }

    public void setCredit(boolean credit) {
        this.credit = credit;
    }

    @NotNull
    public Boolean isDebit() {
        return debit;
    }

    public void setDebit(@NotNull Boolean debit) {
        this.debit = debit;
    }

    @NotNull
    public Double getAmount() {
        return amount;
    }

    public void setAmount(@NotNull Double amount) {
        this.amount = amount;
    }

    @NotNull
    public String getAccID() {
        return accID;
    }

    public void setAccID(@NotNull String accID) {
        this.accID = accID;
    }

    @NotNull
    public Double getNewTotal() {
        return newTotal;
    }

    public void setNewTotal(@NotNull Double newTotal) {
        this.newTotal = newTotal;
    }

    @NotNull
    public LocalDate getDate() {
        return date;
    }

    public void setDate(@NotNull LocalDate date) {
        this.date = date;
    }

    @NotNull
    public LocalTime getTime() {return time;}
    public void setTime(@NotNull LocalTime time) {this.time = time;}
    @NotNull
    public String getTransactionType() { return transactionType;}
    public void setTransactionType(@NotNull String transactionType) {this.transactionType = transactionType;}
    public void setTransactionID(@NotNull String transactionID){this.transactionID = transactionID;}
    @NotNull
    public String getTransactionID(){return transactionID;}

    @Override
    public String[] convertToCSV() {
        return new String[]{transactionID, String.valueOf(credit), String.valueOf(debit), transactionType,
                String.valueOf(amount), String.valueOf(newTotal), accID, DateConverter.convertDateToString(date),
                time.toString()};
    }

    @Override
    public int compareTo(@NotNull Transaction o) {
        if (date.isEqual(o.date)) {
            return time.compareTo(o.time) * -1;
        }
        return date.compareTo(o.date) * -1;
    }

    @Override
    public Transaction copy() {
        return new Transaction(this);
    }
}
