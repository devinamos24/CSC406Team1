package edu.missouriwestern.csc406team1.database.model.account;

import org.jetbrains.annotations.NotNull;

public class ATMCard {

    @NotNull
    CheckingAccount account;

    public ATMCard(@NotNull CheckingAccount account) {
        this.account = account;
    }

    @NotNull
    public CheckingAccount getAccount() {
        return account;
    }
}
