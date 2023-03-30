package edu.missouriwestern.csc406team1.database.dao;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AccountDao {
    void addAccount(Account account);

    @NotNull
    ArrayListFlow<Account> getAccounts();

    @Nullable
    Account getAccount(String accountNumber);

    void updateAccount(Account account);
    void deleteAccount(String accountNumber);
    boolean save();
}
