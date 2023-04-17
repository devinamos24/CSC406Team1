package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.database.AccountRepository;
import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;

/**
 * View model for the bank account screen.
 */
public class BankAccountScreenViewModel{

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    private final MutableStateFlow<Customer> customer = new MutableStateFlow<>(null);
    private final MutableStateFlow<Account> account = new MutableStateFlow<>(null);
    private final Function2<String, String, Unit> onTransfer;
    private final Function2<String, String, Unit> onWithdraw;
    private final Function2<String, String, Unit> onDeposit;
    Function0<Unit> onBack;


    /**
     * Constructs a new instance of the view model.
     *
     * @param accountRepository  The repository for managing account data.
     * @param customerRepository The repository for managing customer data.
     * @param onTransfer1
     * @param onWithdraw1
     * @param onDeposit1
     */
    public BankAccountScreenViewModel(AccountRepository accountRepository, CustomerRepository customerRepository,
                                      String ssn, String id,
                                      Function2<String, String, Unit> onTransfer,
                                      Function2<String, String, Unit> onWithdraw,
                                      Function2<String, String, Unit> onDeposit,
                                      Function0<Unit> onBack, Function2<String, String, Unit> onTransfer1, Function2<String, String, Unit> onWithdraw1, Function2<String, String, Unit> onDeposit1) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;

        this.onTransfer = onTransfer1;
        this.onWithdraw = onWithdraw1;
        this.onDeposit = onDeposit1;
    }

    /**
     * Initializes the view model with the given customer SSN and account ID.
     *
     * @param customerSSN The SSN of the customer.
     * @param accountId The ID of the account.
     */
    public void init(String customerSSN, String accountId) {
        customerRepository.getCustomerBySSN(customerSSN)
                .onEach(c -> customer.setValue(c))
                .launchIn(viewModelScope);

        accountRepository.getAccountById(accountId)
                .onEach(a -> account.setValue(a))
                .launchIn(viewModelScope);
    }

    /**
     * Gets the current customer data.
     *
     * @return A state flow of the current customer.
     */
    public StateFlow<Customer> getCustomer() {
        return customer;
    }

    /**
     * Gets the current account data.
     *
     * @return A state flow of the current account.
     */
    public StateFlow<Account> getAccount() {
        return account;
    }

    /**
     * Transfers money from the specified source account to the specified destination account.
     *
     * @param sourceAccountId The ID of the source account.
     * @param destinationAccountId The ID of the destination account.
     */
    public void transfer(String sourceAccountId, String destinationAccountId) {
        // TODO: Implement transfer logic
    }

    /**
     * Withdraws money from the specified account.
     *
     * @param accountId The ID of the account to withdraw from.
     */
    public void withdraw(String accountId) {
        // TODO: Implement withdraw logic
    }

    /**
     * Deposits money into the specified account.
     *
     * @param accountId The ID of the account to deposit into.
     */
    public void deposit(String accountId) {
        // TODO: Implement deposit logic
    }
}
