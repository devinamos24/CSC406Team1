package edu.missouriwestern.csc406team1.database.dao;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.Customer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This interface holds everything you need to be a customer data access object
 * This is mainly for testing purposes as we can make a CustomerDaoTestImpl for testing
 */
public interface CustomerDao {
    /**
     * Adds a customer to the data store.
     *
     * @param customer The customer object to be added.
     * @return true if the customer is added successfully, false otherwise.
     */
    boolean addCustomer(Customer customer);
    /**
     * Retrieves all customers from the data store.
     *
     * @return An ArrayListFlow containing all customers.
     */
        @NotNull
    ArrayListFlow<Customer> getCustomers();
    /**
     * Retrieves a customer from the data store by their SSN.
     *
     * @param ssn The Social Security Number of the customer to be retrieved.
     * @return The customer object if found, null otherwise.
     */
    @Nullable
    Customer getCustomer(String ssn);
    /**
     * Updates a customer's information in the data store.
     *
     * @param customer The customer object containing the updated information.
     */
    void updateCustomer(Customer customer);
    /**
     * Deletes a customer from the data store by their SSN.
     *
     * @param ssn The Social Security Number of the customer to be deleted.
     */
    void deleteCustomer(String ssn);
    /**
     * Saves any changes made to the data store.
     *
     * @return true if the changes are saved successfully, false otherwise.
     */
    boolean save();
}
