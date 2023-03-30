package edu.missouriwestern.csc406team1.database.model.account;

import edu.missouriwestern.csc406team1.util.DateConverter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SavingsAccount extends Account {

    // Constructor for the SavingsAccount class
    public SavingsAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull Date dateOpened, @NotNull Double interestRate) {
        // Call the constructor of the superclass (Account) with the provided balance, dateOpened, and interestRate
        super(accountNumber, customerSSN, balance, dateOpened, interestRate);
    }

    @Override
    public String[] convertToCSV() {
        String[] base = super.convertToCSV();
        ArrayList<String> returnValue = new ArrayList<>(Arrays.asList(base));
        returnValue.add(String.valueOf(getInterestRate()));

        return (String[]) returnValue.toArray();
    }

    // TODO: implement business logic of savings account

}
