package edu.missouriwestern.csc406team1.database.model.loan;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ShortTermLoan extends Loan {

    public ShortTermLoan(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance,
                         @NotNull Double interestRate, @NotNull LocalDate datePaymentDue,
                         @NotNull LocalDate paymentNotified, @NotNull Double currentPaymentDue,
                         @NotNull LocalDate dateSinceLastPayment, @NotNull Boolean missedPayment) {
        super(accountNumber, customerSSN, balance, interestRate, datePaymentDue, paymentNotified, currentPaymentDue,
                dateSinceLastPayment, missedPayment);
    }

    private ShortTermLoan(ShortTermLoan loan) {
        super(loan.getAccountNumber(), loan.getCustomerSSN(), loan.getBalance(), loan.getInterestRate(), loan.getDatePaymentDue(), loan.getPaymentNotified(), loan.getCurrentPaymentDue(), loan.getDateSinceLastPayment(), loan.getMissedPayment());
    }
    // TODO: implement business logic

    public ShortTermLoan copy() {
        return new ShortTermLoan(this);
    }

    @Override
    public String[] convertToCSV() {
        String[] base = super.convertToCSV();
        ArrayList<String> returnValue = new ArrayList<>(Arrays.asList(base));
        returnValue.add("ls");

        return returnValue.toArray(new String[returnValue.size()]);
    }
}
