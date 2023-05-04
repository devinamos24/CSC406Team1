package edu.missouriwestern.csc406team1.database.model.account;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoldDiamondAccount extends CheckingAccount {

    @NotNull
    private static final Double defaultMinimumBalance = 5000.0; //minimum balanced required to not pay transaction fees
    @NotNull
    private Double minimumBalance;

    public GoldDiamondAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull LocalDate dateOpened, @NotNull Boolean isActive, @NotNull Double interestRate, @Nullable SavingsAccount backupAccount, @NotNull Boolean hasATMCard, @NotNull Integer overdraftsThisMonth) {
        super(accountNumber, customerSSN, balance, dateOpened, isActive, interestRate, (balance >= defaultMinimumBalance) ? 0.0 : 0.75, backupAccount, overdraftsThisMonth, hasATMCard);
        this.minimumBalance = defaultMinimumBalance;
    }

    private GoldDiamondAccount(GoldDiamondAccount account) {
        super(account.getAccountNumber(), account.getCustomerSSN(), account.getBalance(), account.getDateOpened(), account.getIsActive(), account.getInterestRate(), account.minimumBalance, account.getBackupAccount(), account.overdraftsThisMonth, account.getAtmCard() != null);
        this.minimumBalance = account.minimumBalance;
    }

    // TODO: make a factory for the GoldDiamondAccount because of all the different combinations

    @NotNull
    public Double getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(@NotNull Double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    @NotNull
    @Override
    public Double getTransactionFee() {
        if (getBalance() < defaultMinimumBalance) {
            return 0.75;
        }
        return 0.0;
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
        List<String> returnValue = new ArrayList<>(Arrays.asList(base));
        returnValue.add("GD");
        returnValue.add(String.valueOf(getInterestRate()));
        returnValue.add(backupID);
        if (this.atmCard == null) {
            returnValue.add("false");
        } else {
            returnValue.add("true");
        }
        returnValue.add(String.valueOf(overdraftsThisMonth));

        return returnValue.toArray(new String[returnValue.size()]);
    }

    @Override
    public GoldDiamondAccount copy() {
        return new GoldDiamondAccount(this);
    }
}
