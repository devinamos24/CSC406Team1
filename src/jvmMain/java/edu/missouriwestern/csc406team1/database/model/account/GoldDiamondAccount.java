package edu.missouriwestern.csc406team1.database.model.account;

import org.jetbrains.annotations.NotNull;
import java.util.Date;

public class GoldDiamondAccount extends CheckingAccount{

    @NotNull
    private static final Double defaultMinimumBalance = 5000.0; //minimum balanced required to not pay transaction fees
    @NotNull
    private Double minimumBalance;

    //constructor
    public GoldDiamondAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull Date dateOpened, @NotNull Double interestRate , @NotNull SavingsAccount backupAccount, @NotNull Double minimumBalance) {
        super(accountNumber, customerSSN, balance, dateOpened, interestRate, (balance >= minimumBalance) ? 0.0: 0.75, backupAccount);
        this.minimumBalance = minimumBalance;
    }

    public GoldDiamondAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull Date dateOpened, @NotNull Double interestRate, @NotNull SavingsAccount backupAccount) {
        super(accountNumber, customerSSN, balance, dateOpened, interestRate, (balance >= defaultMinimumBalance) ? 0.0: 0.75, backupAccount);
        this.minimumBalance = defaultMinimumBalance;
    }

    public GoldDiamondAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull Date dateOpened, @NotNull Double interestRate, @NotNull Double minimumBalance) {
        super(accountNumber, customerSSN, balance, dateOpened, interestRate, (balance >= minimumBalance) ? 0.0: 0.75, null);
        this.minimumBalance = minimumBalance;
    }

    // TODO: make a factory for the GoldDiamondAccount because of all the different combinations

    @NotNull
    public Double getMinimumBalance() { return minimumBalance; }
    public void setMinimumBalance(@NotNull Double minimumBalance) { this.minimumBalance = minimumBalance; }
}
