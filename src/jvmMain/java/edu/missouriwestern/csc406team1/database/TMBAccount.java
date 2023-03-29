package edu.missouriwestern.csc406team1.database;

import org.jetbrains.annotations.NotNull;
import java.util.Date;

public class TMBAccount extends CheckingAccount {


    private static final Double defaultTransactionFee = 0.75;

    //constructor
    public TMBAccount(@NotNull Double balance, @NotNull Date dateOpened, @NotNull SavingsAccount backupAccount) {
        super(balance, dateOpened, null, defaultTransactionFee, backupAccount);
    }

    public TMBAccount(@NotNull Double balance, @NotNull Date dateOpened) {
        super(balance, dateOpened, null, defaultTransactionFee, null);
    }
}
