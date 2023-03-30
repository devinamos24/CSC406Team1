package edu.missouriwestern.csc406team1.database.model.account;

import edu.missouriwestern.csc406team1.util.DateConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class GoldDiamondAccount extends CheckingAccount{

    @NotNull
    private static final Double defaultMinimumBalance = 5000.0; //minimum balanced required to not pay transaction fees
    @NotNull
    private Double minimumBalance;

    //constructor
    public GoldDiamondAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull Date dateOpened, @NotNull Double interestRate , @Nullable SavingsAccount backupAccount, @NotNull Double minimumBalance, @NotNull Integer overdraftsThisMonth) {
        super(accountNumber, customerSSN, balance, dateOpened, interestRate, (balance >= minimumBalance) ? 0.0: 0.75, backupAccount, overdraftsThisMonth);
        this.minimumBalance = minimumBalance;
    }

    public GoldDiamondAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull Date dateOpened, @NotNull Double interestRate, @Nullable SavingsAccount backupAccount, @NotNull Integer overdraftsThisMonth) {
        super(accountNumber, customerSSN, balance, dateOpened, interestRate, (balance >= defaultMinimumBalance) ? 0.0: 0.75, backupAccount, overdraftsThisMonth);
        this.minimumBalance = defaultMinimumBalance;
    }

    // TODO: make a factory for the GoldDiamondAccount because of all the different combinations

    @NotNull
    public Double getMinimumBalance() { return minimumBalance; }
    public void setMinimumBalance(@NotNull Double minimumBalance) { this.minimumBalance = minimumBalance; }

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
        returnValue.add(String.valueOf(getInterestRate()));
        returnValue.add(backupID);
        returnValue.add(String.valueOf(overdraftsThisMonth));

        return (String[]) returnValue.toArray();
    }
}
