package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.database.AccountRepository;
import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.TransactionRepository;
import edu.missouriwestern.csc406team1.database.model.Transaction;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

import java.time.LocalDate;
import java.time.LocalTime;

public class DepositMoneyScreenViewModel {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    //private final MutableStateFlow<Customer> customer = new MutableStateFlow<>(null);
    //private final MutableStateFlow<Account> account = new MutableStateFlow<Account>(null);
    //private final MutableStateFlow<String> selectedAccountId = new MutableStateFlow<>("");

    private final String ssn;
    private final String id;
    private final Function0<Unit> onBack;

    public DepositMoneyScreenViewModel(AccountRepository accountRepository, CustomerRepository customerRepository,
                                        TransactionRepository transactionRepository, String ssn, String id, Function0<Unit> onBack) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
        this.ssn = ssn;
        this.id = id;
        this.onBack = onBack;
    }


    /**
     * Deposits money into the specified account.
     *
     * @param accountId The ID of the account to deposit into.
     * @param amountToDeposit amount to be deposited to account.
     */
    public void deposit(String accountId, Double amountToDeposit) {
        Account account = accountRepository.getAccount(accountId);
        if (account != null) {
            account.setBalance(account.getBalance() + amountToDeposit);
            if (accountRepository.update(account)) {
                transactionRepository.addTransaction(new Transaction("", true, false, "c", amountToDeposit, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                //TODO is there a transaction fee for a deposit?

            }
        }
    }
}
