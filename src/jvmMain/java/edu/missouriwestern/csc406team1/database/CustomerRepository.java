package edu.missouriwestern.csc406team1.database;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.Customer;
/**
 * This interface holds everything you need to be a customer repository
 * This is mainly for testing purposes as we can make a CustomerRepositoryTestImpl for testing
 */
public interface CustomerRepository {
    Customer getCustomer(String ssn);
    ArrayListFlow<Customer> getCustomers();
    void addCustomer(Customer customer);
    void update(Customer customer);
    void delete(Customer customer);
    boolean save();
}
