package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.database.AccountRepository;
import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;

import java.util.List;

/**
 * View model for the bank account screen.
 */
public class BankAccountScreenViewModel{

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

//    private final MutableStateFlow<Customer> customer = new MutableStateFlow<>(null);
//    private final MutableStateFlow<Account> account = new MutableStateFlow<>(null);

    private final String ssn;
    private final String id;

    private final Function2<String, String, Unit> onTransfer;
    private final Function2<String, String, Unit> onWithdraw;
    private final Function2<String, String, Unit> onDeposit;
    private final Function0<Unit> onBack;


    /**
     * Constructs a new instance of the view model.
     *
     * @param accountRepository  The repository for managing account data.
     * @param customerRepository The repository for managing customer data.
     */
    public BankAccountScreenViewModel(
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            String ssn,
            String id,
            Function2<String, String, Unit> onTransfer,
            Function2<String, String, Unit> onWithdraw,
            Function2<String, String, Unit> onDeposit,
            Function0<Unit> onBack
    ) {

        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.ssn = ssn;
        this.id = id;
        this.onTransfer = onTransfer;
        this.onWithdraw = onWithdraw;
        this.onDeposit = onDeposit;
        this.onBack = onBack;
    }

    /**
     * Initializes the view model with the given customer SSN and account ID.
     *
     * @param customerSSN The SSN of the customer.
     * @param accountId The ID of the account.
     */
//    public void init(String customerSSN, String accountId) {
//        customerRepository.getCustomerBySSN(customerSSN)
//                .onEach(c -> customer.setValue(c))
//                .launchIn(viewModelScope);
//
//        accountRepository.getAccountById(accountId)
//                .onEach(a -> account.setValue(a))
//                .launchIn(viewModelScope);
//    }

    /**
     * Gets the current customer data.
     *
     * @return A state flow of the current customer.
     */
    public StateFlow<List<Customer>> getCustomers() {
        return customerRepository.getCustomers().getFlow();
        // It turns out that filtering kotlin flows in java is a headache, so we are just going to give the screen all the customers
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

    public String getSsn() {
        return ssn;
    }

    public String getId() {
        return id;
    }

    public void onTransfer(String ssn, String id) {
        onTransfer.invoke(ssn, id);
    }

    public void onWithdraw(String ssn, String id) {
        onWithdraw.invoke(ssn, id);
    }

    public void onDeposit(String ssn, String id) {
        onDeposit.invoke(ssn, id);
    }

    public void onBack() {
        onBack.invoke();
    }


    /**
     * Withdraws money from the specified account.
     *
     * @param accountId The ID of the account to withdraw from.
     */
    public void withdraw(String accountId) {
        // TODO: Implement withdraw logic
        // NOTE: The withdraw logic will be in the transfer money screen so this can be moved to that view-model
    }

    /**
     * Deposits money into the specified account.
     *
     * @param accountId The ID of the account to deposit into.
     */
    public void deposit(String accountId) {
        // TODO: Implement deposit logic
        // NOTE: The deposit logic will be in the transfer money screen so this can be moved to that view-model
    }
}
