package edu.missouriwestern.csc406team1.database.dao;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.loan.CreditCardLoan;
import edu.missouriwestern.csc406team1.database.model.loan.Loan;
import edu.missouriwestern.csc406team1.database.model.loan.MortgageLoan;
import edu.missouriwestern.csc406team1.database.model.loan.ShortTermLoan;
import edu.missouriwestern.csc406team1.util.CSVWriter;
import edu.missouriwestern.csc406team1.util.DateConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoanDaoImpl implements LoanDao{
    //modified arrayList of loan objects

    @NotNull
    private final ArrayListFlow<Loan> loans = new ArrayListFlow<>();

    private int highestID = 0;

    //the filename for the starting data
    private final String basefilename = "/loan_base.csv";
    //the filename for the new data to be saved to
    private final String filename = "/loan.csv";

    /*
        This constructor attempts to populate the modified arrayList of loans from disk
     */
    public LoanDaoImpl(){
        //Create a list of string arrays to hold each piece of the loan data
        List<String[]> collect = new ArrayList<>();

        //Try to open a stream of data from the saved loan file
        try (Stream<String> info = Files.lines(Paths.get("src", "jvmMain", "resources", filename))) {
            // Split the lines into pieces using the comma as a delimiter
            collect = info.map(line ->line.split(","))
                    .collect(Collectors.toList());

            // If the first file fails, attempt to load the base data set
        } catch (IOException | NullPointerException e) {
            // Try to open a stream of data from the base loan file
            try (Stream<String> info = Files.lines(Paths.get("src", "jvmMain", "resources", basefilename))) {
                //Split the lines into pieces using the comma as a delimiter
                collect = info.map(line -> line.split(","))
                        .collect(Collectors.toList());

                //if the second file fails, print the stacktrace and exit
            } catch (IOException | NullPointerException ee) {
                System.err.println("Error parsing loan resources");
                e.printStackTrace();
                ee.printStackTrace();
                System.exit(1);
            }
        }
        // This number keeps track of what line we are on for logging purposes
        int linenumber = 1;

        // For each list of arguments, create and add a loan from them
        for (String[] args : collect) {
            try {

                if (Integer.parseInt(args[0]) > highestID) {
                    highestID = Integer.parseInt(args[0]);
                }

                switch (args[9]) {
                    case "cc":
                        loans.add(new CreditCardLoan(args[0]/*account number*/, args[1]/*customerSSN*/,
                                Double.parseDouble(args[2])/*current balance*/, Double.parseDouble(args[3])/*current interest rate*/,
                                DateConverter.convertStringToDate(args[4])/*date payment due*/, DateConverter.convertStringToDate(args[5])/*date notified of payment*/,
                                Double.parseDouble(args[6])/*current payment due*/,DateConverter.convertStringToDate(args[7])/*date since last payment made*/,
                                Boolean.parseBoolean(args[9]), Double.parseDouble(args[10])));
                        break;
                    case "ll":
                        loans.add(new MortgageLoan(args[0]/*accountNumber*/, args[1]/*customerSSN*/,
                                Double.parseDouble(args[2])/*balance*/, Double.parseDouble(args[3])/*interestRate*/,
                                DateConverter.convertStringToDate(args[4])/*datePaymentDue*/,
                                DateConverter.convertStringToDate(args[5])/*paymentNotified*/,
                                Double.parseDouble(args[6])/*currentPaymentDue*/,
                                DateConverter.convertStringToDate(args[7])/*dateSinceLastPayment*/,
                                Boolean.parseBoolean(args[8])/*missedPayment*/));
                        break;
                    case "ls":
                        loans.add(new ShortTermLoan(
                                args[0]/*accountNumber*/, args[1]/*customerSSN*/,
                                Double.parseDouble(args[2])/*balance*/, Double.parseDouble(args[3])/*interestRate*/,
                                DateConverter.convertStringToDate(args[4])/*datePaymentDue*/,
                                DateConverter.convertStringToDate(args[5])/*paymentNotified*/,
                                Double.parseDouble(args[6])/*currentPaymentDue*/,
                                DateConverter.convertStringToDate(args[7])/*dateSinceLastPayment*/,
                                Boolean.parseBoolean(args[8])/*missedPayment*/));
                        break;
                    default:
                        throw new IllegalArgumentException("Type: "+ args[9] + " not supported!");
                }

                // If the params are malformed, skip the line and log it
            } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
                System.err.println("Parse error on line: " + linenumber + " in loan database");
                e.printStackTrace();
                //TODO put the problem line in an error file to be fixed by bank later
            } catch (DateTimeParseException e) {
                System.err.println("Date parse error on line: " + linenumber + " in loan database");
            }
            // Increase line number
            linenumber++;
        }

    }
    /**
     * Adds a loan to the data store, assigning it a unique account number.
     *
     * @param loan The loan object to be added.
     * @return true if the loan is added successfully, false otherwise.
     */
    @Override
    public boolean addLoan(Loan loan) {
        loan.setAccountNumber(String.valueOf(highestID+1));
        highestID++;
        return loans.add(loan);
    }
    /**
     * Retrieves all loans from the data store.
     *
     * @return An ArrayListFlow containing all loans.
     */
    @NotNull
    @Override
    public ArrayListFlow<Loan> getLoans() {
        return loans;
    }
    /**
     * Retrieves a loan from the data store by its account number.
     *
     * @param accountNumber The account number of the loan to be retrieved.
     * @return The loan object if found, null otherwise.
     */
    @Nullable
    @Override
    public Loan getLoan(String accountNumber) {
        for (Loan loan : loans) {
            if (loan.getAccountNumber().equals(accountNumber)) {
                return loan;
            }
        }
        return null;
    }
    /**
     * Updates a loan's information in the data store.
     *
     * @param loan The loan object containing the updated information.
     * @return true if the loan is updated successfully, false otherwise.
     */
    @Override
    public boolean updateLoan(Loan loan) {
        for (Loan loan1 : loans) {
            if (loan1.getAccountNumber().equals(loan.getAccountNumber())) {
                if (loans.remove(loan1)) {
                    return loans.add(loan.copy());
                }
            }
        }
        return false;
    }
    /**
     * Deletes a loan from the data store by its account number.
     *
     * @param accountNumber The account number of the loan to be deleted.
     */
    @Override
    public void deleteLoan(String accountNumber) {
        for (Loan loan : loans) {
            if (loan.getAccountNumber().equals(accountNumber)) {
                loans.remove(loan);
                return;
            }
        }
        System.err.println("Error deleting loan, could not find matching ID");
    }
    /**
     * Saves any changes made to the data store.
     *
     * @return true if the changes are saved successfully, false otherwise.
     */
    @Override
    public boolean save() {
        try {
            return CSVWriter.writeToCsvFile(loans, filename);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
