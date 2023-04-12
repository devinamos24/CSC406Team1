package edu.missouriwestern.csc406team1.database.dao;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.Transaction;
import edu.missouriwestern.csc406team1.database.model.Check;
import edu.missouriwestern.csc406team1.database.model.Purchase;
import edu.missouriwestern.csc406team1.util.CSVWriter;
import edu.missouriwestern.csc406team1.util.DateConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionDaoImpl implements TransactionDao{
    @NotNull
    private final ArrayListFlow<Transaction> transactions = new ArrayListFlow<>();

    private int highestID = 0;

    //the filename for the starting data
    private final String basefilename = "/transaction_base.csv";
    //the filename for the new data to be saved to
    private final String filename = "/transaction.csv";

    /**
     * Constructor for the TransactionDaoImpl class.
     * It attempts to populate the ArrayListFlow of transactions from disk.
     */
    public TransactionDaoImpl() {
        // Create a list of string arrays to hold each piece of the transaction data
        List<String[]> collect = new ArrayList<>();

        // Try to open a stream of data from the saved Transaction file
        try (Stream<String> info = Files.lines(Paths.get("src", "jvmMain", "resources", filename))) {
            // Split the lines into pieces using the comma as a delimiter
            collect = info.map(line -> line.split(","))
                    .collect(Collectors.toList());

            // If the first file fails, attempt to load the base data set
        } catch (IOException | NullPointerException e) {
            // Try to open a stream of data from the base loan file
            try (Stream<String> info = Files.lines(Paths.get("src", "jvmMain", "resources", basefilename))) {
                // Split the lines into pieces using the comma as a delimiter
                collect = info.map(line -> line.split(","))
                        .collect(Collectors.toList());

                // If the second file fails, print the stack trace and exit
            } catch (IOException | NullPointerException ee) {
                System.err.println("Error parsing transaction resources");
                e.printStackTrace();
                ee.printStackTrace();
                System.exit(1);
            }
        }

        // This number keeps track of what line we are on for logging purposes
        int linenumber = 1;

        // For each list of arguments, create and add a transaction from them
        for (String[] args : collect) {
            try {

                String transactionID = args[0];
                boolean credit = Boolean.parseBoolean(args[1]);
                boolean debit = Boolean.parseBoolean(args[2]);
                String transactionType = args[3];
                double amount = Double.parseDouble(args[4]);
                double newTotal = Double.parseDouble(args[5]);
                String accID = args[6];
                LocalDate date = LocalDate.parse(args[7]);
                LocalTime time = LocalTime.parse(args[8]);
                String purchaseMerchant = args[9].equals("null") ? null : args[9];
                String purchaseCategory = args[10].equals("null") ? null : args[10];
                boolean purchaseIsPosted = Boolean.parseBoolean(args[11]);
                int checkNumber = args[12].equals("null") ? 0 : Integer.parseInt(args[12]);
                String checkPayee = args[13].equals("null") ? null : args[13];
                boolean checkStopPayment = Boolean.parseBoolean(args[14]);

                Transaction transaction;
                if (Integer.parseInt(args[0]) > highestID) {
                    highestID = Integer.parseInt(args[0]);
                }
                switch (transactionType.toLowerCase()) {
                    case "p":
                        transaction = new Purchase(transactionID, credit, debit, transactionType, amount, newTotal,
                                accID, date, time, purchaseMerchant, purchaseCategory, purchaseIsPosted);
                        break;
                    case "ch":
                        transaction = new Check(transactionID, credit, debit, transactionType, amount, newTotal, accID,
                                date, time, checkNumber, checkPayee, checkStopPayment);
                        break;
                    default:
                        transaction = new Transaction(transactionID, credit, debit, transactionType, amount, newTotal,
                                accID, date, time);
                }

                transactions.add(transaction);
                // If the params are malformed, skip the line and log it
            } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
                System.err.println("Parse error on line: " + linenumber + " in transaction database");
                e.printStackTrace();
                //TODO: put the problem line in an error file to be fixed by bank later
            } catch (DateTimeParseException e) {
                System.err.println("Date parse error on line: " + linenumber + " in transaction database");
            }
            // Increase line number
            linenumber++;
        }
    }

    @Override
    public void addTransaction(Transaction transaction) {
        transaction.setTransactionID(String.valueOf(highestID+1));
        highestID++;
        transactions.add(transaction);
    }

    @NotNull
    @Override
    public ArrayListFlow<Transaction> getTransactions() {return transactions;}

    @Nullable
    @Override
    public Transaction getTransaction(String transactionID) {
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionID().equals(transactionID)){
                return transaction;
            }
        }
        return null;
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        for (Transaction transaction1 : transactions){
            if (transaction1.getTransactionID().equals(transaction.getTransactionID())) {
                transactions.remove(transaction1);
                transactions.add(transaction.copy());
                return;
            }
        }
    }

    @Override
    public void deleteTransaction(String transactionID) {
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionID().equals(transactionID)) {
                transactions.remove(transaction);
                return;
            }
        }
        System.err.println("Error deleting transaction, could not find matching ID");
    }

    @Override
    public boolean save() {
        try {
            return CSVWriter.writeToCsvFile(transactions, filename);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
