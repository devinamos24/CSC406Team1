package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.database.AccountRepository;
import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;

import java.util.List;

public class TransferMoneyScreenViewModel {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    //private final MutableStateFlow<Customer> customer = new MutableStateFlow<>(null);
    //private final MutableStateFlow<Account> account = new MutableStateFlow<Account>(null);
    //private final MutableStateFlow<String> selectedAccountId = new MutableStateFlow<>("");

    private final String ssn;
    private final String id;
    private final Function0<Unit> onBack;

    public TransferMoneyScreenViewModel(AccountRepository accountRepository, CustomerRepository customerRepository,
                                        String ssn, String id, Function0<Unit> onBack) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.ssn = ssn;
        this.id = id;
        this.onBack = onBack;
    }

    public StateFlow<List<Customer>> getCustomer() {
        return customerRepository.getCustomers().getFlow();
    }

    public StateFlow<List<Account>> getAccount() {
        return accountRepository.getAccounts().getFlow();
    }

    public String getId() {
        return id;
    }

    public String getSsn() {
        return ssn;
    }

    /**
     * Transfers money from the specified source account to the specified destination account.
     *
     * @param sourceAccountId The ID of the source account.
     * @param destinationAccountId The ID of the destination account.
     */
    public void transfer(String sourceAccountId, String destinationAccountId) {
        Account destinationAccount = accountRepository.getAccount(destinationAccountId);
        Account sourceAccount = accountRepository.getAccount(sourceAccountId);
        if (destinationAccount != null && sourceAccount != null) {
            //TODO: implement transfer logic
        }
    }

    /**
     * Withdraws money from the specified account.
     *
     * @param accountId The ID of the account to withdraw from.
     */
    public void withdraw(String accountId) {
        Account account = accountRepository.getAccount(accountId);
        if (account != null) {
            //TODO: implement withdraw logic
        }
    }

    /**
     * Deposits money into the specified account.
     *
     * @param accountId The ID of the account to deposit into.
     */
    public void deposit(String accountId) {
        Account account = accountRepository.getAccount(accountId);
        if (account != null) {
            //TODO: implement deposit logic
        }
    }
}

