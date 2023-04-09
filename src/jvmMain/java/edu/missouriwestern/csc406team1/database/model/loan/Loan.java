package edu.missouriwestern.csc406team1.database.model.loan;

import edu.missouriwestern.csc406team1.database.model.account.Account;
import edu.missouriwestern.csc406team1.util.CSV;
import edu.missouriwestern.csc406team1.util.DateConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.Date;

public abstract class Loan implements CSV, Comparable<Loan>{
    @NotNull
    private String accountNumber;//account number to reference the account
    @NotNull
    String customerSSN;// SSN of customer owning the account
    @NotNull
    private Date paymentDue; //date next payment is due
    @Nullable
    private Date paymentNotified; //date notification is sent for next payment
    @NotNull
    private Double currentPaymentDue; //amount due for next payment
    @NotNull
    private Date dateSinceLastPayment; //date last payment was recieved
    @NotNull
    private Boolean missedPayment; //flag if a payment was missed

    //Should Loan and account be a single object that the other types of accounts extend from?
    //NOTE: The other loan types can extend from the base loan class.
    //However, you must keep in mind that they need to customize some of their own functionality
    //The below attributes are also in account.
    @NotNull
    private Double interestRate;
    @NotNull
    private Double balance;//current balance of the account
    @NotNull
    private LocalDate dateOpened;//date the account was opened with the bank

    public Loan(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Date paymentDue, @NotNull Double currentPaymentDue, @NotNull Date dateSinceLastPayment, @NotNull Double interestRate, @NotNull Double balance, @NotNull LocalDate dateOpened) {
        this.accountNumber = accountNumber;
        this.customerSSN = customerSSN;
        this.paymentDue = paymentDue;
        this.paymentNotified = null;
        this.currentPaymentDue = currentPaymentDue;
        this.dateSinceLastPayment = dateSinceLastPayment;
        this.missedPayment = false;
        this.interestRate = interestRate;
        this.balance = balance;
        this.dateOpened = dateOpened;
    }
    public int compareTo(Loan o) {
        return this.dateOpened.compareTo(o.dateOpened);
    }

    @NotNull
    public Date getPaymentDue() { return paymentDue; }
    public void setPaymentDue(@NotNull Date paymentDue) { this.paymentDue = paymentDue; }

    @Nullable
    public Date getPaymentNotified() { return paymentNotified; }
    public void setPaymentNotified(@Nullable Date paymentNotified) { this.paymentNotified = paymentNotified; }

    @NotNull
    public Double getCurrentPaymentDue() { return currentPaymentDue; }
    public void setCurrentPaymentDue(@NotNull Double currentPaymentDue) { this.currentPaymentDue = currentPaymentDue; }

    @NotNull
    public Date getDateSinceLastPayment() { return dateSinceLastPayment; }
    public void setDateSinceLastPayment(@NotNull Date dateSinceLastPayment) { this.dateSinceLastPayment = dateSinceLastPayment; }

    @NotNull
    public Boolean hasMissedPayment() { return missedPayment; }
    public void setMissedPayment(@NotNull Boolean missedPayment) { this.missedPayment = missedPayment; }

    @Override
    public String[] convertToCSV() {
        return new String[] {accountNumber, customerSSN, String.valueOf(balance), DateConverter.convertDateToString(dateOpened)};
    }
}
