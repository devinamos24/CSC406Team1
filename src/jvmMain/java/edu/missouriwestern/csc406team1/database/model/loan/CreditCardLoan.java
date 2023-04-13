package edu.missouriwestern.csc406team1.database.model.loan;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.time.LocalDate;

public class CreditCardLoan extends Loan{
    @NotNull
    private double creditLimit;

    public CreditCardLoan(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance,
                          @NotNull Double interestRate, @NotNull LocalDate datePaymentDue,
                          @NotNull LocalDate paymentNotified, @NotNull Double currentPaymentDue,
                          @NotNull LocalDate dateSinceLastPayment, @NotNull Boolean missedPayment,
                          @NotNull Double creditLimit) {
        super(accountNumber, customerSSN, balance, interestRate, datePaymentDue, paymentNotified, currentPaymentDue,
                dateSinceLastPayment, missedPayment);
        this.creditLimit = creditLimit;
    }
    // TODO: implement business logic

}
