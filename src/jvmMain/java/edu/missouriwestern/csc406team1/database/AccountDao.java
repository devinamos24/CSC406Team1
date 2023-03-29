package edu.missouriwestern.csc406team1.database;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AccountDao {
    void addAccount(Account account);

    @NotNull
    ArrayListFlow<Account> getAccount();

    @Nullable
    Account getAccount(String accountNumber);

    void updateAccount(Account account);
    void deleteCustomer(String accountNumber);
    boolean save();
}
