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
    @Nullable // The atm card associated with this checking account
    ATMCard atmCard;
    /**
     * Constructs a new CheckingAccount object with the specified parameters.
     *
     * @param accountNumber      The account number of the checking account.
     * @param customerSSN        The customer's Social Security number associated with the account.
     * @param balance            The initial balance of the account.
     * @param dateOpened         The date the account was opened.
     * @param isActive           The active status of the account.
     * @param interestRate       The interest rate associated with the account.
     * @param transactionFee     The transaction fee for this checking account.
     * @param backupAccount      The backup savings account, if available.
     * @param overdraftsThisMonth The number of overdrafts for this account in the current month.
     * @param hasATMCard         Indicates whether this checking account has an associated ATM card.
     */
    public CheckingAccount(@NotNull String accountNumber, @NotNull String customerSSN, @NotNull Double balance, @NotNull LocalDate dateOpened, @NotNull Boolean isActive, @Nullable Double interestRate, @NotNull Double transactionFee, @Nullable SavingsAccount backupAccount, @NotNull Integer overdraftsThisMonth, @NotNull Boolean hasATMCard) {
        super(accountNumber, customerSSN, balance, dateOpened, isActive, interestRate);
        this.transactionFee = transactionFee;
        this.backupAccount = backupAccount;
        this.overdraftsThisMonth = overdraftsThisMonth;
        if (hasATMCard) {
            atmCard = new ATMCard(this);
        }
    }

    @NotNull
    public Double getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(@NotNull Double transactionFee) {
        this.transactionFee = transactionFee;
    }

    @Nullable
    public SavingsAccount getBackupAccount() {
        return backupAccount;

    }
    public void setBackupAccount(@Nullable SavingsAccount backupAccount) {
        this.backupAccount = backupAccount;
    }

    @NotNull
    public Integer getOverdraftsThisMonth() {
        return overdraftsThisMonth;
    }

    public void setOverdraftsThisMonth(@NotNull Integer overdraftsThisMonth) {
        this.overdraftsThisMonth = overdraftsThisMonth;
    }

    @Nullable
    public ATMCard getAtmCard() {
        return atmCard;
    }

    public void setAtmCard(@Nullable ATMCard atmCard) {
        this.atmCard = atmCard;
    }
}
