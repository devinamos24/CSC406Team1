package edu.missouriwestern.csc406team1.database.dao;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.util.CSVWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomerDaoImpl implements CustomerDao {

    // A modified arraylist of customer objects
    @NotNull
    private final ArrayListFlow<Customer> customers = new ArrayListFlow<>();

    // The filename for the starting data
    private final String basefilename = "/customer_base.csv";
    // The filename for the new data to be saved to
    private final String filename = "/customer.csv";

    /**
     * This constructor attempts to populate the modified arraylist of customers from disk
     */
    public CustomerDaoImpl(){
        // Create a list of string arrays to hold each piece of customer data
        List<String[]> collect = new ArrayList<>();

        // Try to open a stream of data from the saved customer file
        try (Stream<String> info = Files.lines(Paths.get("src", "jvmMain", "resources", filename))) {
            // Split the lines into pieces using the comma as a delimiter
            collect = info.map(line -> line.split(","))
                    .collect(Collectors.toList());

            // If the first file fails, attempt to load the base data set
        } catch (IOException | NullPointerException e) {
            // Try to open a stream of data from the base customer file
            try (Stream<String> info = Files.lines(Paths.get("src", "jvmMain", "resources", basefilename))) {
                // Split the lines into pieces using the comma as a delimiter
                collect = info.map(line -> line.split(","))
                        .collect(Collectors.toList());

                // If the second file fails, print the stacktrace and exit
            } catch (IOException | NullPointerException ee) {
                System.err.println("Error parsing customer resources");
                e.printStackTrace();
                ee.printStackTrace();
                System.exit(1);
            }
        }
        // This number keeps track of what line we are on for logging purposes
        int linenumber = 0;

        // For each list of arguments, create and add a customer from them
        for (String[] args : collect) {
            try {
                customers.add(new Customer(args[0], args[1], args[2], args[3], args[4], args[5], args[6]));

                // If the params are malformed, skip the line and log it
            } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
                System.err.println("Parse error on line: " + linenumber + " in customer base.database");
                //TODO put the problem line in an error file to be fixed by bank later
            }
            // Increase line number
            linenumber++;
        }
    }

    /**
     * This method attemps to add a customer to the system, if the ssn is already taken it will print an error and fail
     * @param customer the customer to be added to the system
     */
    @Override
    public void addCustomer(Customer customer) {
         if (customers.stream().anyMatch(customer1 -> customer1.getSsn().equals(customer.getSsn()))) {
             System.err.println("Could not add customer due to ssn not being unique");
         } else {
             customers.add(customer);
         }
    }

    /**
     * This method returns a modified arraylist of customer objects
     * @return base.ArrayListFlow of customer objects
     */
    @Override
    @NotNull
    public ArrayListFlow<Customer> getCustomers() {
        return customers;
    }

    /**
     * This method attempts to retrieve customer data based on their ssn
     * @param ssn the social security number of the customer to be retrieved
     * @return the customer requested, or null if they do not exist
     */
    @Override
    @Nullable
    public Customer getCustomer(String ssn) {
        for (Customer customer : customers) {
            if (customer.getSsn().equals(ssn)) {
                return customer;
            }
        }
        return null;
    }

    /**
     * This method attempts to find and update a customer using the ssn
     * @param customer the new data for the customer to be updated
     */
    @Override
    public void updateCustomer(Customer customer) {
        for (Customer customer1 : customers) {
            if (customer1.getSsn().equals(customer.getSsn())) {
                customers.remove(customer1);
                customers.add(customer);
                return;
            }
        }
    }

    /**
     * This method attempts to find and delete a customer
     * @param ssn the social security number of the customer to be deleted
     */
    @Override
    public void deleteCustomer(String ssn) {
        for (Customer customer : customers) {
            if (customer.getSsn().equals(ssn)) {
                customers.remove(customer);
                return;
            }
        }
        System.err.println("Error deleting customer, could not find matching ssn");
    }

    /**
     * This method attempts to write our customer data to disk
     * @return true if the file was successfully saved, false if otherwise
     */
    @Override
    public boolean save() {
        try {
            return CSVWriter.writeToCsvFile(customers, filename);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}