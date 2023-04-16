package edu.missouriwestern.csc406team1.database;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.dao.AccountDao;
import edu.missouriwestern.csc406team1.database.dao.AccountDaoImpl;
import edu.missouriwestern.csc406team1.database.model.account.Account;

public class AccountRepositoryImpl implements AccountRepository {

    private final AccountDao accountDao;

    public AccountRepositoryImpl() {
        accountDao = new AccountDaoImpl();
    }

    @Override
    public Account getAccount(String accountNumber) {
        return accountDao.getAccount(accountNumber);
    }

    @Override
    public ArrayListFlow<Account> getAccounts() {
        return accountDao.getAccounts();
    }

    @Override
    public void addAccount(Account account) {
        accountDao.addAccount(account);
    }

    @Override
    public boolean update(Account account) {
        return accountDao.updateAccount(account);
    }

    @Override
    public void delete(String id) {
        accountDao.deleteAccount(id);
    }

    @Override
    public boolean save() {
        return accountDao.save();
    }
}
