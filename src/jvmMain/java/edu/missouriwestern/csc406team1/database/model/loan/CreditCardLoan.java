package edu.missouriwestern.csc406team1.database.model.loan;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class CreditCardLoan extends Loan{
    @NotNull
    private Double creditLimit;

    public CreditCardLoan(
            @NotNull String accountNumber,
            @NotNull String customerSSN,
            @NotNull Double balance,
            @NotNull Double interestRate,
            @NotNull LocalDate datePaymentDue,
            @NotNull LocalDate paymentNotified,
            @NotNull Double currentPaymentDue,
            @NotNull LocalDate dateSinceLastPayment,
            @NotNull Boolean missedPayment,
            @NotNull Double creditLimit
    ) {
        super(accountNumber, customerSSN, balance, interestRate, datePaymentDue, paymentNotified, currentPaymentDue,
                dateSinceLastPayment, missedPayment);
        this.creditLimit = creditLimit;
    }

    private CreditCardLoan(CreditCardLoan loan) {
        super(loan.getAccountNumber(), loan.getCustomerSSN(), loan.getBalance(), loan.getInterestRate(), loan.getDatePaymentDue(), loan.getPaymentNotified(), loan.getCurrentPaymentDue(), loan.getDateSinceLastPayment(), loan.getMissedPayment());
        this.creditLimit = loan.creditLimit;
    }
    // TODO: implement business logic

    @NotNull
    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(@NotNull Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    @Override
    public CreditCardLoan copy() {
        return new CreditCardLoan(this);
    }

    @Override
    public String[] convertToCSV() {
        String[] base = super.convertToCSV();
        ArrayList<String> returnValue = new ArrayList<>(Arrays.asList(base));
        returnValue.add("cc");
        returnValue.add(String.valueOf(creditLimit));

        return returnValue.toArray(new String[returnValue.size()]);
    }
}
