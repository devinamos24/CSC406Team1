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

public class DepositMoneyScreenViewModel {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final MutableStateFlow<Boolean> hasFailed = StateFlowKt.MutableStateFlow(false);
    private final MutableStateFlow<InputWrapper> amount = StateFlowKt.MutableStateFlow(new InputWrapper());
    private final String ssn;
    private final String id;
    private final Function0<Unit> onBack;

    public DepositMoneyScreenViewModel(
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
    }

    public StateFlow<List<Account>> getAccounts() {
        return accountRepository.getAccounts().getFlow();
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

    public void onAmountChange(String newAmount) {
        newAmount = newAmount.replaceAll("\\D", "");
        amount.setValue(new InputWrapper(newAmount, null));
    }

    public void onDeposit() {
        Account account = accountRepository.getAccount(id);
        double money;
        try {
            money = Double.parseDouble(amount.getValue().component1()) / 100;
        } catch (NumberFormatException e) {
            hasFailed.setValue(true);
            return;
        }
        double fee = 0.0;
        if (account instanceof TMBAccount) {
            fee = ((TMBAccount) account).getTransactionFee();
        } else if (account instanceof GoldDiamondAccount) {
            fee = ((GoldDiamondAccount) account).getTransactionFee();
        } else if (account instanceof SavingsAccount) {
            fee = 0.75;
        }

        if (account != null && money > fee) {
            account.setBalance(account.getBalance() + money);
            if (accountRepository.update(account)) {
                transactionRepository.addTransaction(new Transaction("", true, false, "c", money, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                if (fee > 0.0) {
                    transactionRepository.addTransaction(new Transaction("", false, true, "f", fee, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                }
                return;
            }
        }
        hasFailed.setValue(true);
    }

    public void onBack() {
        onBack.invoke();
    }
}
