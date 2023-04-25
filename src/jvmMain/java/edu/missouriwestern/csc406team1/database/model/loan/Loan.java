package edu.missouriwestern.csc406team1.database.model.loan;

import edu.missouriwestern.csc406team1.util.CSV;
import edu.missouriwestern.csc406team1.util.Copyable;
import edu.missouriwestern.csc406team1.util.DateConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

public abstract class Loan implements CSV, Comparable<Loan>, Copyable<Loan> {
    @NotNull
    private String accountNumber;//account number to reference the account
    @NotNull
    private String customerSSN;// SSN of customer owning the account
    @NotNull
    private LocalDate datePaymentDue; //date next payment is due
    @Nullable
    private LocalDate paymentNotified; //date notification is sent for next payment
    @NotNull
    private Double currentPaymentDue; //amount due for next payment
    @NotNull
    private LocalDate dateSinceLastPayment; //date last payment was recieved
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

    public Loan(@NotNull String accountNumber,
                @NotNull String customerSSN,
                @NotNull Double balance,
                @NotNull Double interestRate,
                @NotNull LocalDate datePaymentDue,
                @NotNull LocalDate paymentNotified,
                @NotNull Double currentPaymentDue,
                @NotNull LocalDate dateSinceLastPayment,
                @NotNull Boolean missedPayment
    ) {
                //TODO for ease of use, order of constructor needs to follow order of CVS data
                //TODO super constructor order for each child class will need updated to reflect this change
        this.accountNumber = accountNumber;
        this.customerSSN = customerSSN;
        this.balance = balance;
        this.interestRate = interestRate;
        this.datePaymentDue = datePaymentDue;
        this.paymentNotified = paymentNotified;
        this.dateSinceLastPayment = dateSinceLastPayment;
        this.currentPaymentDue = currentPaymentDue;
        this.missedPayment = missedPayment;
        this.dateOpened = LocalDate.now(); // TODO: Implement date opened into database for loan

    }
    public int compareTo(Loan o) {
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
    public LocalDate getDatePaymentDue() { return datePaymentDue; }
    public void setDatePaymentDue(@NotNull LocalDate datePaymentDue) { this.datePaymentDue = datePaymentDue; }

    @Nullable
    public LocalDate getPaymentNotified() { return paymentNotified; }
    public void setPaymentNotified(@Nullable LocalDate paymentNotified) { this.paymentNotified = paymentNotified; }

    @NotNull
    public Double getCurrentPaymentDue() { return currentPaymentDue; }
    public void setCurrentPaymentDue(@NotNull Double currentPaymentDue) { this.currentPaymentDue = currentPaymentDue; }

    @NotNull
    public LocalDate getDateSinceLastPayment() { return dateSinceLastPayment; }
    public void setDateSinceLastPayment(@NotNull LocalDate dateSinceLastPayment) { this.dateSinceLastPayment = dateSinceLastPayment; }

    @NotNull
    public Boolean hasMissedPayment() { return missedPayment; }
    public void setMissedPayment(@NotNull Boolean missedPayment) { this.missedPayment = missedPayment; }

    @NotNull
    public String getCustomerSSN() {
        return customerSSN;
    }

    public void setCustomerSSN(@NotNull String customerSSN) {
        this.customerSSN = customerSSN;
    }

    @NotNull
    public Boolean getMissedPayment() {
        return missedPayment;
    }

    @NotNull
    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(@NotNull Double interestRate) {
        this.interestRate = interestRate;
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

    @Override
    public String[] convertToCSV() {
        return new String[] {accountNumber, customerSSN, String.valueOf(balance), DateConverter.convertDateToString(dateOpened)};
    }
}
