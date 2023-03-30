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
    void addCustomer(Customer customer);
    @NotNull
    ArrayListFlow<Customer> getCustomers();
    @Nullable
    Customer getCustomer(String ssn);
    void updateCustomer(Customer customer);
    void deleteCustomer(String ssn);
    boolean save();
}
