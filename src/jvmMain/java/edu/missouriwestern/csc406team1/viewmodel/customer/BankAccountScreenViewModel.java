package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.database.AccountRepository;
import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.flow.StateFlow;

import java.util.List;

/**
 * View model for the bank account screen.
 */
public class BankAccountScreenViewModel {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final String ssn;
    private final String id;
    private final Function2<String, String, Unit> onTransfer;
    private final Function2<String, String, Unit> onWithdraw;
    private final Function2<String, String, Unit> onDeposit;
    private final Function2<String, String, Unit> onViewTransactionHistory;
    private final Function2<String, String, Unit> onSelectBackupAccount;
    private final Function0<Unit> onBack;


    /**
     * Constructs a new instance of the view model.
     *
     * @param accountRepository  The repository for managing account data.
     * @param customerRepository The repository for managing customer data.
     */
    public BankAccountScreenViewModel(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            String ssn,
            String id,
            Function2<String, String, Unit> onTransfer,
            Function2<String, String, Unit> onWithdraw,
            Function2<String, String, Unit> onDeposit,
            Function2<String, String, Unit> onViewTransactionHistory,
            Function2<String, String, Unit> onSelectBackupAccount,
            Function0<Unit> onBack
    ) {

        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.ssn = ssn;
        this.id = id;
        this.onTransfer = onTransfer;
        this.onWithdraw = onWithdraw;
        this.onDeposit = onDeposit;
        this.onViewTransactionHistory = onViewTransactionHistory;
        this.onSelectBackupAccount = onSelectBackupAccount;
        this.onBack = onBack;
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

    public void onTransfer() {
        onTransfer.invoke(ssn, id);
    }

    public void onWithdraw() {
        onWithdraw.invoke(ssn, id);
    }

    public void onDeposit() {
        onDeposit.invoke(ssn, id);
    }

    public void onViewTransactionHistory() {
        onViewTransactionHistory.invoke(ssn, id);
    }

    public void onSelectBackupAccount() {
        onSelectBackupAccount.invoke(ssn, id);
    }

    public void onBack() {
        onBack.invoke();
    }

}
