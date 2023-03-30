package edu.missouriwestern.csc406team1.database.model.account;

import edu.missouriwestern.csc406team1.util.DateConverter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class TMBAccount extends CheckingAccount {


    private static final Double defaultTransactionFee = 0.75;

    //constructor
    public TMBAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull Date dateOpened, @NotNull SavingsAccount backupAccount, @NotNull Integer overdraftsThisMonth) {
        super(accountNumber, customerSSN, balance, dateOpened, null, defaultTransactionFee, backupAccount, overdraftsThisMonth);
    }

    public TMBAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull Date dateOpened, @NotNull Integer overdraftsThisMonth) {
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
        returnValue.add(backupID);
        returnValue.add(String.valueOf(overdraftsThisMonth));

        return (String[]) returnValue.toArray();
    }
}
