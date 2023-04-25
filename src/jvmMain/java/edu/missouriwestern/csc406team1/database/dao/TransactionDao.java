package edu.missouriwestern.csc406team1.database.dao;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TransactionDao {
    /**
     * Adds a transaction to the data store.
     *
     * @param transaction The transaction object to be added.
     */
    void addTransaction(Transaction transaction);
    /**
     * Retrieves all transactions from the data store.
     *
     * @return An ArrayListFlow containing all transactions.
     */
    @NotNull
    ArrayListFlow<Transaction> getTransactions();
    /**
     * Retrieves a transaction from the data store by its transaction ID.
     *
     * @param transactionID The ID of the transaction to be retrieved.
     * @return The transaction object if found, null otherwise.
     */
    @Nullable
    Transaction getTransaction(String transactionID);
    /**
     * Updates a transaction's information in the data store.
     *
     * @param transaction The transaction object containing the updated information.
     */
    void updateTransaction(Transaction transaction);
    /**
     * Deletes a transaction from the data store by its transaction ID.
     *
     * @param transactionID The ID of the transaction to be deleted.
     */
    void deleteTransaction(String transactionID);
    /**
     * Saves any changes made to the data store.
     *
     * @return true if the changes are saved successfully, false otherwise.
     */
    boolean save();
}
