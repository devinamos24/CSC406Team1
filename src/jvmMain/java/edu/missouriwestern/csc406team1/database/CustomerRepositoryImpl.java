package edu.missouriwestern.csc406team1.database;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.dao.CustomerDao;
import edu.missouriwestern.csc406team1.database.dao.CustomerDaoImpl;
import edu.missouriwestern.csc406team1.database.model.Customer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is an implementation of the base.CustomerRepository interface
 * It serves as a layer of abstraction between our DAO and our business logic
 */
public class CustomerRepositoryImpl implements CustomerRepository {

    // This DAO object pulls our data from disk and manages it in memory
    private final CustomerDao customerDao;

    // A simple constructor, it instantiates our DAO object
    public CustomerRepositoryImpl() {
        customerDao = new CustomerDaoImpl();
    }

    // Get a customer from their ssn
    @Override
    @Nullable
    public Customer getCustomer(String ssn) {
        return customerDao.getCustomer(ssn);
    }

    // Get a custom arraylist of all customers in the system
    @Override
    @NotNull
    public ArrayListFlow<Customer> getCustomers() {
        return customerDao.getCustomers();
    }

    // Add a customer to the system
    @Override
    public boolean addCustomer(Customer customer) {
        return customerDao.addCustomer(customer);
    }

    // Update a customer that is already in the system
    @Override
    public void update(Customer customer) {
        customerDao.updateCustomer(customer);
    }

    // Delete a customer from the system
    @Override
    public void delete(String ssn) {
        customerDao.deleteCustomer(ssn);
    }

    // Attempt to save the customers to disk
    @Override
    public boolean save() {
        return customerDao.save();
    }
}
