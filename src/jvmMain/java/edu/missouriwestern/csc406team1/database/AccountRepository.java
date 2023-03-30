package edu.missouriwestern.csc406team1.database;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.database.model.account.Account;

public interface AccountRepository {
    Account getAccount(String accountID);
    ArrayListFlow<Account> getAccounts();
    void addAccount(Account account);
    void update(Account account);
    void delete(Account account);
    boolean save();
}
