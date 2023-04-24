package edu.missouriwestern.csc406team1.database.model.account;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class SavingsAccount extends Account {

    // Constructor for the SavingsAccount class
    public SavingsAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull LocalDate dateOpened, @NotNull Boolean isActive, @NotNull Double interestRate) {
        // Call the constructor of the superclass (Account) with the provided balance, dateOpened, and interestRate
        super(accountNumber, customerSSN, balance, dateOpened, isActive, interestRate);
    }

    @Override
    public String[] convertToCSV() {
        String[] base = super.convertToCSV();
        ArrayList<String> returnValue = new ArrayList<>(Arrays.asList(base));
        returnValue.add("S");
        returnValue.add(String.valueOf(getInterestRate()));

        return returnValue.toArray(new String[returnValue.size()]);
    }

    // TODO: implement business logic of savings account

}
