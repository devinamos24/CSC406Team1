package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.database.AccountRepository;
import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.TransactionRepository;
import edu.missouriwestern.csc406team1.database.model.Transaction;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import edu.missouriwestern.csc406team1.database.model.account.GoldDiamondAccount;
import edu.missouriwestern.csc406team1.database.model.account.SavingsAccount;
import edu.missouriwestern.csc406team1.database.model.account.TMBAccount;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

import java.time.LocalDate;
import java.time.LocalTime;

public class WithdrawMoneyScreenViewModel {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    //private final MutableStateFlow<Customer> customer = new MutableStateFlow<>(null);
    //private final MutableStateFlow<Account> account = new MutableStateFlow<Account>(null);
    //private final MutableStateFlow<String> selectedAccountId = new MutableStateFlow<>("");

    private final String ssn;
    private final String id;
    private final Function0<Unit> onBack;

    public WithdrawMoneyScreenViewModel(AccountRepository accountRepository, CustomerRepository customerRepository,
                                        TransactionRepository transactionRepository, String ssn, String id, Function0<Unit> onBack) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
        this.ssn = ssn;
        this.id = id;
        this.onBack = onBack;
    }

    /**
     * Withdraws money from the specified account.
     *
     * @param accountId The ID of the account to withdraw from.
     * @param amountToWithdraw amount to withdraw from account.
     */
    public Boolean withdraw(String accountId, Double amountToWithdraw) {
        Account account = accountRepository.getAccount(accountId);
        Boolean processed=true;
        Double fee = null;
        if (account != null) {
            if (account.getBalance() >= amountToWithdraw) {
                account.setBalance(account.getBalance() - amountToWithdraw);
                if (accountRepository.update(account)) {
                    transactionRepository.addTransaction(new Transaction("", false, true, "d", amountToWithdraw, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                    if (account instanceof TMBAccount) {

                        fee = .75;
                    } else if (account instanceof GoldDiamondAccount) {

                        if (account.getBalance() < 5000) fee=.75;
                    }
                    if (fee!=null) {
                        //add transaction for the fee
                        transactionRepository.addTransaction(new Transaction("", false, true, "f", fee, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                    }
                }
            }
            else {
                processed = false;
            }
        }
        return processed;
    }
}
