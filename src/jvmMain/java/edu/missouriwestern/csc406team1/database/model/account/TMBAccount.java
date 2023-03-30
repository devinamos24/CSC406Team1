package edu.missouriwestern.csc406team1.database.model.account;

import org.jetbrains.annotations.NotNull;
import java.util.Date;

public class TMBAccount extends CheckingAccount {


    private static final Double defaultTransactionFee = 0.75;

    //constructor
    public TMBAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull Date dateOpened, @NotNull SavingsAccount backupAccount) {
        super(accountNumber, customerSSN, balance, dateOpened, null, defaultTransactionFee, backupAccount);
    }

    public TMBAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull Date dateOpened) {
        super(accountNumber, customerSSN, balance, dateOpened, null, defaultTransactionFee, null);
    }
}
