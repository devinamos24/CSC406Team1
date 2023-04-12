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
import edu.missouriwestern.csc406team1.util.InputWrapper;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TransferMoneyScreenViewModel {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final MutableStateFlow<Boolean> hasFailed = StateFlowKt.MutableStateFlow(false);
    private final MutableStateFlow<InputWrapper> amount = StateFlowKt.MutableStateFlow(new InputWrapper());
    private final MutableStateFlow<String> selectedAccountId = StateFlowKt.MutableStateFlow("");
    private final String ssn;
    private final String id;
    private final Function0<Unit> onBack;

    public TransferMoneyScreenViewModel(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            String ssn,
            String id,
            Function0<Unit> onBack
    ) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.ssn = ssn;
        this.id = id;
        this.onBack = onBack;
    }

    public StateFlow<List<Customer>> getCustomers() {
        return customerRepository.getCustomers().getFlow();
        // It turns out that filtering kotlin flows in java is a headache, so we are just going to give the screen all the customers
    }

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

    public StateFlow<InputWrapper> getAmount() {
        return FlowKt.asStateFlow(amount);
    }

    public StateFlow<Boolean> getHasFailed() {
        return FlowKt.asStateFlow(hasFailed);
    }

    public StateFlow<String> getSelectedAccountId() {
        return FlowKt.asStateFlow(selectedAccountId);
    }

    public void onSelectAccount(String id) {
        selectedAccountId.setValue(id);
    }

    public void onClickAll() {
        amount.setValue(new InputWrapper(String.valueOf(Double.valueOf(accountRepository.getAccount(id).getBalance() * 100).intValue()), null));
    }

    public void onClickNone() {
        amount.setValue(new InputWrapper());
    }

    public void onAmountChange(String newAmount) {
        newAmount = newAmount.replaceAll("\\D", "");
        amount.setValue(new InputWrapper(newAmount, null));
    }

    public void onTransfer() {
        Account sourceAccount = accountRepository.getAccount(id);
        Account destinationAccount = accountRepository.getAccount(selectedAccountId.getValue());
        double money;
        try {
            money = Double.parseDouble(amount.getValue().component1()) / 100;
        } catch (NumberFormatException e) {
            hasFailed.setValue(true);
            return;
        }
        double fee = 0.0;
        if (sourceAccount instanceof TMBAccount) {
            fee = ((TMBAccount) sourceAccount).getTransactionFee();
        } else if (sourceAccount instanceof GoldDiamondAccount) {
            fee = ((GoldDiamondAccount) sourceAccount).getTransactionFee();
        } else if (sourceAccount instanceof SavingsAccount) {
            fee = 0.75;
        }

        if (sourceAccount != null && destinationAccount != null) {
            if (sourceAccount.getBalance() > money + fee && money != 0.0) {
                sourceAccount.setBalance(sourceAccount.getBalance() - money - fee);
                if (accountRepository.update(sourceAccount)) {
                    transactionRepository.addTransaction(new Transaction("", false, true, "t", money, sourceAccount.getBalance(), sourceAccount.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                    if (fee > 0.0) {
                        transactionRepository.addTransaction(new Transaction("", false, true, "f", fee, sourceAccount.getBalance(), sourceAccount.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                    }

                    destinationAccount.setBalance(destinationAccount.getBalance() + money);
                    if (accountRepository.update(destinationAccount)) {
                        transactionRepository.addTransaction(new Transaction("", true, false, "t", money, destinationAccount.getBalance(), destinationAccount.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                        return;
                    } else {
                        sourceAccount.setBalance(sourceAccount.getBalance() + money + fee);
                        accountRepository.update(sourceAccount);
                    }
                }
            }
        }
        hasFailed.setValue(true);
    }

    public void onBack() {
        onBack.invoke();
    }
}

