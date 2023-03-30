package edu.missouriwestern.csc406team1.database.model.account;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

public abstract class CheckingAccount extends Account{

    @NotNull
    private Double transactionFee;//cost of fee per transaction
    @Nullable
    private SavingsAccount backupAccount;//pointer to the account designated as the backup account if setup (must be savings account)
    @NotNull Integer overdraftsThisMonth;

    public CheckingAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull LocalDate dateOpened, @Nullable Double interestRate, @NotNull Double transactionFee, @Nullable SavingsAccount backupAccount, @NotNull Integer overdraftsThisMonth) {
        super(accountNumber, customerSSN, balance, dateOpened, interestRate);
        this.transactionFee = transactionFee;
        this.backupAccount = backupAccount;
        this.overdraftsThisMonth = overdraftsThisMonth;
    }

    @NotNull
    public Double getTransactionFee() { return transactionFee; }
    public void setTransactionFee(@NotNull Double transactionFee) { this.transactionFee = transactionFee; }

    @Nullable
    public SavingsAccount getBackupAccount() { return backupAccount; }
    public void setBackupAccount(@Nullable SavingsAccount backupAccount) { this.backupAccount = backupAccount; }

    @NotNull
    public Integer getOverdraftsThisMonth() {
        return overdraftsThisMonth;
    }

    public void setOverdraftsThisMonth(Integer overdraftsThisMonth) {
        this.overdraftsThisMonth = overdraftsThisMonth;
    }
}
