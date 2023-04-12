package edu.missouriwestern.csc406team1.database;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.dao.TransactionDao;
import edu.missouriwestern.csc406team1.database.dao.TransactionDaoImpl;
import edu.missouriwestern.csc406team1.database.model.Transaction;

public class TransactionRepositoryImpl implements TransactionRepository{


    private final TransactionDao transactionDao;

    public TransactionRepositoryImpl() {
        transactionDao = new TransactionDaoImpl();
    }

    @Override
    public Transaction getTransaction(String transactionID) {
        return transactionDao.getTransaction(transactionID);
    }

    @Override
    public ArrayListFlow<Transaction> getTransactions() {
        return transactionDao.getTransactions();
    }

    @Override
    public void addTransaction(Transaction transaction) {
        transactionDao.addTransaction(transaction);
    }

    @Override
    public void update(Transaction transaction) {
        transactionDao.updateTransaction(transaction);
    }

    @Override
    public void delete(Transaction transaction) {
        transactionDao.deleteTransaction(transaction.getTransactionID());
    }

    @Override
    public boolean save() {
        return transactionDao.save();
    }
}
