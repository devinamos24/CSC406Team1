package edu.missouriwestern.csc406team1.database;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.Transaction;

public interface TransactionRepository {
    Transaction getTransaction(String transactionID);
    ArrayListFlow<Transaction> getTransactions();
    void addTransaction(Transaction transaction);
    void update(Transaction transaction);
    void delete(String id);
    boolean save();
}
