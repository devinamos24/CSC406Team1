package edu.missouriwestern.csc406team1.database.dao;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AccountDaoImpl implements AccountDao{

    //modified arrayList of account objects
    @NotNull
    private final ArrayListFlow<Account> accounts = new ArrayListFlow<>();

    //The filename for the starting data,
    private final String basefilename = "/account_base.csv";
    //the filename for the new data to be saved to
    private final String filename = "/account.csv";

    /*
        This constructor attempts to populate the modified arrayList of accounts from disk
     */
    public AccountDaoImpl(){
        //Create a list of string arrays to hold each piece of account data
        List<String[]> collect = new ArrayList<>();

        //Try to open a stream of data from the saved account file
        //TODO finish the rest of this DaoImpl
    }

    @Override
    public void addAccount(Account account) {

    }

    @NotNull
    @Override
    public ArrayListFlow<Account> getAccounts() {
        return null;
    }

    @Nullable
    @Override
    public Account getAccount(String accountNumber) {
        return null;
    }

    @Override
    public void updateAccount(Account account) {

    }

    @Override
    public void deleteAccount(String accountNumber) {

    }

    @Override
    public boolean save() {
        return false;
    }
}
