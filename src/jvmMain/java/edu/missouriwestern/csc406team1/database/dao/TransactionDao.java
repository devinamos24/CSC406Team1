package edu.missouriwestern.csc406team1.database.dao;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TransactionDao {

    void addTransaction(Transaction transaction);

    @NotNull
    ArrayListFlow<Transaction> getTransactions();

    @Nullable
    Transaction getTransaction(String transactionID);

    void updateTransaction(Transaction transaction);
    void deleteTransaction(String transactionID);
    boolean save();
}
