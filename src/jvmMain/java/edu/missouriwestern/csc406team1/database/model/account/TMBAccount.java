package edu.missouriwestern.csc406team1.database.model.account;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class TMBAccount extends CheckingAccount {


    private static final Double defaultTransactionFee = 0.75;

    //constructor
    public TMBAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull LocalDate dateOpened, @NotNull SavingsAccount backupAccount, @NotNull Integer overdraftsThisMonth) {
        super(accountNumber, customerSSN, balance, dateOpened, null, defaultTransactionFee, backupAccount, overdraftsThisMonth);
    }

    public TMBAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull LocalDate dateOpened, @NotNull Integer overdraftsThisMonth) {
        super(accountNumber, customerSSN, balance, dateOpened, null, defaultTransactionFee, null, overdraftsThisMonth);
    }

    @Override
    public String[] convertToCSV() {
        String backupID;
        if (getBackupAccount() == null) {
            backupID = "null";
        } else {
            backupID = getBackupAccount().getAccountNumber();
        }

        String[] base = super.convertToCSV();
        ArrayList<String> returnValue = new ArrayList<>(Arrays.asList(base));
        returnValue.add("TMB");
        returnValue.add(backupID);
        returnValue.add(String.valueOf(overdraftsThisMonth));

        return returnValue.toArray(new String[returnValue.size()]);
    }
}