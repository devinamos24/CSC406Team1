package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.database.AccountRepository;
import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import kotlinx.coroutines.flow.StateFlow;

import java.util.List;

public class SelectBackupAccountScreenViewModel {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final String ssn;
    private final String id;

    /**
    * Constructor for the CustomerSelectBackupAccountViewModel.
    *
    * @param accountRepository Repository for managing account data
    * @param customerRepository Repository for managing customer data
    * @param ssn Social security number of the customer
    * @param id Identifier for the account
    */
    public SelectBackupAccountScreenViewModel(AccountRepository accountRepository,
                                                    CustomerRepository customerRepository,
                                                    String ssn, String id) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.ssn = ssn;
        this.id = id;
    }

    /**
     * Gets the current account data.
     *
     * @return A state flow of the current account.
     */
    public StateFlow<List<Account>> getAccounts() {
        return accountRepository.getAccounts().getFlow();
        // It turns out that filtering kotlin flows in java is a headache, so we are just going to give the screen all the accounts
    }
    /**
     * Gets the current customer data.
     *
     * @return A state flow of the current customer.
     */
    public StateFlow<List<Customer>> getCustomers() {
        return customerRepository.getCustomers().getFlow();
        // It turns out that filtering kotlin flows in java is a headache, so we are just going to give the screen all the customers
    }
}


