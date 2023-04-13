package edu.missouriwestern.csc406team1.database.model.loan;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.time.LocalDate;

public class MortgageLoan extends Loan{

    public MortgageLoan(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance,
                        @NotNull Double interestRate, @NotNull LocalDate datePaymentDue,
                        @NotNull LocalDate paymentNotified, @NotNull Double currentPaymentDue,
                        @NotNull LocalDate dateSinceLastPayment, @NotNull Boolean missedPayment) {
        super(accountNumber, customerSSN, balance, interestRate, datePaymentDue, paymentNotified, currentPaymentDue,
                dateSinceLastPayment, missedPayment);

    }
    // TODO: implement business logic

}
