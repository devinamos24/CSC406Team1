package edu.missouriwestern.csc406team1.database.model.account;

import org.jetbrains.annotations.NotNull;


public class ATMCard {
    /**
     * Represents an ATM card associated with a checking account.
     */
    @NotNull
    CheckingAccount account;
    /**
     * Constructs a new ATMCard object with the associated checking account.
     *
     * @param account The checking account that the ATM card is associated with.
     */
    public ATMCard(@NotNull CheckingAccount account) {
        this.account = account;
    }
    /**
     * Retrieves the checking account associated with this ATM card.
     *
     * @return The associated checking account.
     */
    @NotNull
    public CheckingAccount getAccount() {
        return account;
    }
}
