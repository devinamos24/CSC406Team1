package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.database.AccountRepository;
import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import edu.missouriwestern.csc406team1.database.model.account.CheckingAccount;
import edu.missouriwestern.csc406team1.database.model.account.SavingsAccount;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

import java.util.List;

public class SelectBackupAccountScreenViewModel {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final String ssn;
    private final String id;
    private final MutableStateFlow<String> selectedAccountId;
    private final Function0<Unit> onBack;

    /**
     * Constructor for the CustomerSelectBackupAccountViewModel.
     *
     * @param accountRepository  Repository for managing account data
     * @param customerRepository Repository for managing customer data
     * @param ssn                Social security number of the customer
     * @param id                 Identifier for the account
     */
    public SelectBackupAccountScreenViewModel(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            String ssn,
            String id,
            Function0<Unit> onBack
    ) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.ssn = ssn;
        this.id = id;
        SavingsAccount selectedAccount = ((CheckingAccount) accountRepository.getAccount(id)).getBackupAccount();
        String selectedId = "";
        if (selectedAccount != null) {
            selectedId = selectedAccount.getAccountNumber();
        }
        this.selectedAccountId = StateFlowKt.MutableStateFlow(selectedId);
        this.onBack = onBack;
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
     * Gets the current account data.
     *
     * @return A state flow of the current account.
     */
    public StateFlow<List<Account>> getAccounts() {
        return accountRepository.getAccounts().getFlow();
    }

    public String getSsn() {
        return ssn;
    }

    public String getId() {
        return id;
    }

    public StateFlow<String> getSelectedAccountId() {
        return FlowKt.asStateFlow(selectedAccountId);
    }

    public void onSelectAccount(String id) {
        try {
            CheckingAccount account = (CheckingAccount) accountRepository.getAccount(this.id);
            account.setBackupAccount((SavingsAccount) accountRepository.getAccount(id));
            accountRepository.update(account);
            selectedAccountId.setValue(id);
        } catch (ClassCastException e) {
            CheckingAccount account = (CheckingAccount) accountRepository.getAccount(this.id);
            account.setBackupAccount(null);
            accountRepository.update(account);
            e.printStackTrace();
        }
    }

    public void onBack() {
        onBack.invoke();
    }
}


