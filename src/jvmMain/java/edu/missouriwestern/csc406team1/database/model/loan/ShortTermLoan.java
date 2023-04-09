package edu.missouriwestern.csc406team1.database.model.loan;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.time.LocalDate;

public class ShortTermLoan extends Loan{

    public ShortTermLoan(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Date paymentDue, @NotNull Double currentPaymentDue, @NotNull Date dateSinceLastPayment, @NotNull Double interestRate, @NotNull Double balance, @NotNull LocalDate dateOpened) {
        super(accountNumber, customerSSN, paymentDue, currentPaymentDue, dateSinceLastPayment, interestRate, balance, dateOpened);
    }
    // TODO: implement business logic

}
