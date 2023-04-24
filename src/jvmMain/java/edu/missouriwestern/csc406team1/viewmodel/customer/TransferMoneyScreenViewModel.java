package edu.missouriwestern.csc406team1.viewmodel.customer;


import edu.missouriwestern.csc406team1.database.AccountRepository;
import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.TransactionRepository;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.database.model.Transaction;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import edu.missouriwestern.csc406team1.database.model.account.GoldDiamondAccount;
import edu.missouriwestern.csc406team1.database.model.account.SavingsAccount;
import edu.missouriwestern.csc406team1.database.model.account.TMBAccount;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.flow.StateFlow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TransferMoneyScreenViewModel {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    //private final MutableStateFlow<Customer> customer = new MutableStateFlow<>(null);
    //private final MutableStateFlow<Account> account = new MutableStateFlow<Account>(null);
    //private final MutableStateFlow<String> selectedAccountId = new MutableStateFlow<>("");

    private final String ssn;
    private final String id;
    private final Function0<Unit> onBack;

    public TransferMoneyScreenViewModel(AccountRepository accountRepository, CustomerRepository customerRepository,
                                        TransactionRepository transactionRepository, String ssn, String id, Function0<Unit> onBack) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
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
    public Boolean transfer(String sourceAccountId, String destinationAccountId, Double amountToTransfer) {
        Account sourceAccount = accountRepository.getAccount(sourceAccountId);
        Account destinationAccount = accountRepository.getAccount(destinationAccountId);
        Boolean processed=true;

        if (sourceAccount != null && destinationAccount != null) {
            if (sourceAccount.getBalance() >= amountToTransfer) {
                // Deduct the amount from the source account
                sourceAccount.setBalance(sourceAccount.getBalance() - amountToTransfer);
                if (accountRepository.update(sourceAccount)) {
                    transactionRepository.addTransaction(new Transaction("", false, true, "t", amountToTransfer, sourceAccount.getBalance(), sourceAccount.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                    //TODO we could possibly even add a letter to the transfer type saying if the customer got it from the atm or the teller
                    Double fee = null;
                    if (sourceAccount instanceof TMBAccount) {
                        //TODO set transfer fee for TMBAccount
                    } else if (sourceAccount instanceof GoldDiamondAccount) {
                        //TODO set transfer fee for GoldDiamondAccount
                    } else if (sourceAccount instanceof SavingsAccount) {
                        //TODO set transfer fee for Savings account
                    } else {
                        processed=false;
                    }
                    if (fee!=null) {
                        //add transaction for the fee
                        transactionRepository.addTransaction(new Transaction("", false, true, "f", fee, sourceAccount.getBalance(), sourceAccount.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                    }

                    // Add the amount to the destination account
                    destinationAccount.setBalance(destinationAccount.getBalance() + amountToTransfer);
                    if (accountRepository.update(destinationAccount)) {
                        transactionRepository.addTransaction(new Transaction("", true, false, "t", amountToTransfer, destinationAccount.getBalance(), destinationAccount.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                    }
                }
            } else {
                processed=false;

            }
        }
        return processed;
    }




}

