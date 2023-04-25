package edu.missouriwestern.csc406team1.database.dao;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AccountDao {
    /**
     * Adds a new account to the data store.
     * @param account the Account object to be added
     * @return true if the account was added successfully, false otherwise
     */
    boolean addAccount(Account account);
    /**
     * Retrieves a list of all accounts in the data store.
     * @return an ArrayListFlow containing all Account objects
     */
    @NotNull
    ArrayListFlow<Account> getAccounts();
    /**
     * Retrieves an account from the data store using its account number.
     * @param accountNumber the account number of the account to be retrieved
     * @return the Account object if found, null otherwise
     */
    @Nullable
    Account getAccount(String accountNumber);
    /**
     * Updates the details of an existing account in the data store.
     * @param account the Account object with updated information
     * @return true if the account was updated successfully, false otherwise
     */
    boolean updateAccount(Account account);
    /**
     * Deletes an account from the data store using its account number.
     * @param accountNumber the account number of the account to be deleted
     */
    void deleteAccount(String accountNumber);
    /**
     * Saves the changes made to the data store, if applicable.
     * @return true if the changes were saved successfully, false otherwise
     */
    boolean save();
}
