package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.database.AccountRepository;
import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.TransactionRepository;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.database.model.Transaction;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import kotlinx.coroutines.flow.StateFlow;
import java.util.List;

public class ViewTransactionHistoryScreenViewModel {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final String ssn;
    private final String accountId;

    /**
     * Constructor for the CustomerViewTransactionHistoryViewModel.
     *
     * @param accountRepository Repository for managing account data
     * @param customerRepository Repository for managing customer data
     * @param transactionRepository Repository for managing transaction data
     * @param ssn Social security number of the customer
     * @param accountId The account ID for which the transactions will be retrieved
     */
    public ViewTransactionHistoryScreenViewModel(AccountRepository accountRepository,
                                                   CustomerRepository customerRepository,
                                                   TransactionRepository transactionRepository,
                                                   String ssn, String accountId) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
        this.ssn = ssn;
        this.accountId = accountId;
    }

    /**
     * Gets the current account data.
     *
     * @return A state flow of the current account.
     */
    public StateFlow<List<Account>> getAccounts() {
        return accountRepository.getAccounts().getFlow();
    }

    /**
     * Gets the current customer data.
     *
     * @return A state flow of the current customer.
     */
    public StateFlow<List<Customer>> getCustomers() {
        return customerRepository.getCustomers().getFlow();
    }

    /**
     * Gets the current transaction data.
     *
     * @return A state flow of the current transactions.
     */
    public StateFlow<List<Transaction>> getTransactions() {
        return transactionRepository.getTransactions().getFlow();
    }
}

