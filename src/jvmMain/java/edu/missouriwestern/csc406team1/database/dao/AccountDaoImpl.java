package edu.missouriwestern.csc406team1.database.dao;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.account.*;
import edu.missouriwestern.csc406team1.util.CSVWriter;
import edu.missouriwestern.csc406team1.util.DateConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountDaoImpl implements AccountDao{

    //modified arrayList of account objects
    @NotNull
    private final ArrayListFlow<Account> accounts = new ArrayListFlow<>();

    private int highestID = 0;

    //The filename for the starting data,
    private final String basefilename = "/account_base.csv";
    //the filename for the new data to be saved to
    private final String filename = "/account.csv";

    /*
        This constructor attempts to populate the modified arrayList of accounts from disk
     */
    public AccountDaoImpl(){
        // Create a list of string arrays to hold each piece of account data
        List<String[]> collect = new ArrayList<>();

        // Try to open a stream of data from the saved account file
        try (Stream<String> info = Files.lines(Paths.get("src", "jvmMain", "resources", filename))) {
            // Split the lines into pieces using the comma as a delimiter
            collect = info.map(line -> line.split(","))
                    .collect(Collectors.toList());

            // If the first file fails, attempt to load the base data set
        } catch (IOException | NullPointerException e) {
            // Try to open a stream of data from the base account file
            try (Stream<String> info = Files.lines(Paths.get("src", "jvmMain", "resources", basefilename))) {
                // Split the lines into pieces using the comma as a delimiter
                collect = info.map(line -> line.split(","))
                        .collect(Collectors.toList());

                // If the second file fails, print the stacktrace and exit
            } catch (IOException | NullPointerException ee) {
                System.err.println("Error parsing account resources");
                e.printStackTrace();
                ee.printStackTrace();
                System.exit(1);
            }
        }
        // This number keeps track of what line we are on for logging purposes
        int linenumber = 1;

        // For each list of arguments, create and add a account from them
        Map<CheckingAccount, String> checkingAccounts = new HashMap<>();
        Map<String, SavingsAccount> savingsAccounts = new HashMap<>();
        for (String[] args : collect) {
            try {
                CheckingAccount account;
                SavingsAccount savingsAccount;
                if (Integer.parseInt(args[0]) > highestID) {
                    highestID = Integer.parseInt(args[0]);
                }
                switch (args[4]) {
                    case "TMB":
                        account = new TMBAccount(args[0], args[1], Double.parseDouble(args[2]), DateConverter.convertStringToDate(args[3]), Boolean.parseBoolean(args[5]), Boolean.parseBoolean(args[7]), Integer.parseInt(args[8]));
                        if (!args[5].equals("null")) {
                            checkingAccounts.put(account, args[6]);
                        }
                        accounts.add(account);
                        break;
                    case "GD":
                        account = new GoldDiamondAccount(args[0], args[1], Double.parseDouble(args[2]), DateConverter.convertStringToDate(args[3]), Boolean.parseBoolean(args[5]), Double.parseDouble(args[6]), null, Boolean.parseBoolean(args[8]), Integer.parseInt(args[9]));
                        if (!args[6].equals("null")) {
                            checkingAccounts.put(account, args[6]);
                        }
                        accounts.add(account);
                        break;
                    case "S":
                        savingsAccount = new SavingsAccount(args[0], args[1], Double.parseDouble(args[2]), DateConverter.convertStringToDate(args[3]), Boolean.parseBoolean(args[5]), Double.parseDouble(args[6]));
                        accounts.add(savingsAccount);
                        savingsAccounts.put(args[0], savingsAccount);
                        break;
                    case "CD":
                        accounts.add(new CDAccount(args[0], args[1], Double.parseDouble(args[2]), DateConverter.convertStringToDate(args[3]), Boolean.parseBoolean(args[5]), Double.parseDouble(args[6]), DateConverter.convertStringToDate(args[7])));
                        break;
                    default:
                        throw new IllegalArgumentException("Type: "+ args[2] + " not supported!");
                }

                // If the params are malformed, skip the line and log it
            } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
                System.err.println("Parse error on line: " + linenumber + " in account database");
                e.printStackTrace();
                //TODO put the problem line in an error file to be fixed by bank later
            } catch (DateTimeParseException e) {
                System.err.println("Date parse error on line: " + linenumber + " in account database");
            }
            // Increase line number
            linenumber++;
        }
        for (CheckingAccount key : checkingAccounts.keySet()) {
                key.setBackupAccount(savingsAccounts.get(checkingAccounts.get(key)));
        }
    }
    /**
     * Adds a new account to the list of accounts and assigns it a new account number.
     * @param account the Account object to be added
     * @return true if the account was added successfully, false otherwise
     */
    @Override
    public boolean addAccount(Account account) {
        account.setAccountNumber(String.valueOf(highestID+1));
        highestID++;
        return accounts.add(account);
    }
    /**
     * Retrieves a list of all accounts.
     * @return an ArrayListFlow containing all Account objects
     */
    @NotNull
    @Override
    public ArrayListFlow<Account> getAccounts() {
        return accounts;
    }
    /**
     * Retrieves an account using its account number.
     * @param accountNumber the account number of the account to be retrieved
     * @return the Account object if found, null otherwise
     */
    @Nullable
    @Override
    public Account getAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
    /**
     * Updates the details of an existing account in the list of accounts.
     * @param account the Account object with updated information
     * @return true if the account was updated successfully, false otherwise
     */
    @Override
    public boolean updateAccount(Account account) {
        for (Account account1 : accounts) {
            if (account1.getAccountNumber().equals(account.getAccountNumber())) {
                if (accounts.remove(account1)) {
                    return accounts.add(account.copy());
                }
            }
        }
        return false;
    }
    /**
     * Deletes an account using its account number.
     * @param accountNumber the account number of the account to be deleted
     */
    @Override
    public void deleteAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                accounts.remove(account);
                return;
            }
        }
        System.err.println("Error deleting account, could not find matching ID");
    }
    /**
     * Saves the changes made to the list of accounts to a CSV file.
     * @return true if the changes were saved successfully, false otherwise
     */
    @Override
    public boolean save() {
        try {
            return CSVWriter.writeToCsvFile(accounts, filename);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
