package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.database.AccountRepository;
import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.TransactionRepository;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.database.model.Transaction;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import edu.missouriwestern.csc406team1.database.model.account.CheckingAccount;
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

public class ShoppingScreenViewModel {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final MutableStateFlow<Boolean> hasFailed = StateFlowKt.MutableStateFlow(false);
    private final MutableStateFlow<String> hasFailedText = StateFlowKt.MutableStateFlow("Purchase Failed, try again");
    private final MutableStateFlow<InputWrapper> amount = StateFlowKt.MutableStateFlow(new InputWrapper());
    private final MutableStateFlow<String> selectedAccountId = StateFlowKt.MutableStateFlow("");
    private final String ssn;
    private final Function0<Unit> onBack;

    public ShoppingScreenViewModel(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            String ssn,
            Function0<Unit> onBack
    ) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.ssn = ssn;
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

    public StateFlow<InputWrapper> getAmount() {
        return FlowKt.asStateFlow(amount);
    }

    public void onAmountChange(String newAmount) {
        newAmount = newAmount.replaceAll("\\D", "");
        amount.setValue(new InputWrapper(newAmount, null));
    }

    public StateFlow<Boolean> getHasFailed() {
        return FlowKt.asStateFlow(hasFailed);
    }

    public StateFlow<String> getHasFailedText() {
        return FlowKt.asStateFlow(hasFailedText);
    }

    public StateFlow<String> getSelectedAccountId() {
        return FlowKt.asStateFlow(selectedAccountId);
    }

    public void onSelectAccount(String id) {
        selectedAccountId.setValue(id);
    }

    public void onMakePurchaseATM() {
        CheckingAccount account;
        double money;
        try {
            account = (CheckingAccount) accountRepository.getAccount(selectedAccountId.getValue());
            money = Double.parseDouble(amount.getValue().component1()) / 100;
        } catch (NumberFormatException e) {
            hasFailedText.setValue("Debiting account failed, try again");
            hasFailed.setValue(true);
            return;
        }
        double fee = account.getTransactionFee();
        if (account.getBalance() >= money) {
            account.setBalance(account.getBalance() - money);
            if (accountRepository.update(account)) {
                transactionRepository.addTransaction(new Transaction("", false, true, "d", money, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                if (fee > 0.0) {
                    transactionRepository.addTransaction(new Transaction("", false, true, "f", fee, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                }
                hasFailed.setValue(false);
                return;
            }
        } else {
            if (account.getBackupAccount() != null && account.getBalance() + account.getBackupAccount().getBalance() >= money) {
                account.getBackupAccount().setBalance(account.getBackupAccount().getBalance() - (money - account.getBalance()) - fee);
                double oldBalance = account.getBalance();
                account.setBalance(0.0);
                if (accountRepository.update(account) && accountRepository.update(account.getBackupAccount())) {
                    transactionRepository.addTransaction(new Transaction("", false, true, "d", oldBalance, account.getBalance() + fee, account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                    transactionRepository.addTransaction(new Transaction("", false, true, "d", money - oldBalance, account.getBackupAccount().getBalance(), account.getBackupAccount().getAccountNumber(), LocalDate.now(), LocalTime.now()));
                    if (fee > 0.0) {
                        transactionRepository.addTransaction(new Transaction("", false, true, "f", fee, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                    }
                    hasFailed.setValue(false);
                } else {
                    hasFailedText.setValue("Debiting account failed, try again");
                    hasFailed.setValue(true);
                }
            } else {
                account.setBalance(account.getBalance() - 25.0);
                account.setOverdraftsThisMonth(account.getOverdraftsThisMonth() + 1);
                if (accountRepository.update(account)) {
                    transactionRepository.addTransaction(new Transaction("", false, true, "f", 25.0, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                }
                hasFailedText.setValue("Insufficient funds in account, applying overdraft fee");
                hasFailed.setValue(true);
            }
            return;
        }
        hasFailedText.setValue("Debiting account failed, try again");
        hasFailed.setValue(true);
    }

    /**
     * Withdraws money from the specified account.
     */
    public void onMakePurchaseCheck() {
        CheckingAccount account;
        double money;
        try {
            account = (CheckingAccount) accountRepository.getAccount(selectedAccountId.getValue());
            money = Double.parseDouble(amount.getValue().component1()) / 100;
        } catch (NumberFormatException e) {
            hasFailedText.setValue("Debiting account failed, try again");
            hasFailed.setValue(true);
            return;
        }
        double fee = account.getTransactionFee();
        if (account.getBalance() >= money) {
            account.setBalance(account.getBalance() - money);
            if (accountRepository.update(account)) {
                transactionRepository.addTransaction(new Transaction("", false, true, "d", money, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                if (fee > 0.0) {
                    transactionRepository.addTransaction(new Transaction("", false, true, "f", fee, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                }
                hasFailed.setValue(false);
                return;
            }
        } else {
            if (account.getBackupAccount() != null && account.getBalance() + account.getBackupAccount().getBalance() >= money) {
                account.getBackupAccount().setBalance(account.getBackupAccount().getBalance() - (money - account.getBalance()) - fee);
                double oldBalance = account.getBalance();
                account.setBalance(0.0);
                if (accountRepository.update(account) && accountRepository.update(account.getBackupAccount())) {
                    transactionRepository.addTransaction(new Transaction("", false, true, "d", oldBalance, account.getBalance() + fee, account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                    transactionRepository.addTransaction(new Transaction("", false, true, "d", money - oldBalance, account.getBackupAccount().getBalance(), account.getBackupAccount().getAccountNumber(), LocalDate.now(), LocalTime.now()));
                    if (fee > 0.0) {
                        transactionRepository.addTransaction(new Transaction("", false, true, "f", fee, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                    }
                    hasFailed.setValue(false);
                } else {
                    hasFailedText.setValue("Debiting account failed, try again");
                    hasFailed.setValue(true);
                }
            } else {
                account.setBalance(account.getBalance() - 25.0);
                account.setOverdraftsThisMonth(account.getOverdraftsThisMonth() + 1);
                if (accountRepository.update(account)) {
                    transactionRepository.addTransaction(new Transaction("", false, true, "f", 25.0, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                }
                hasFailedText.setValue("Insufficient funds in account, applying check bounce fee");
                hasFailed.setValue(true);
            }
            return;
        }
        hasFailedText.setValue("Debiting account failed, try again");
        hasFailed.setValue(true);
    }

    public void onBack() {
        onBack.invoke();
    }
}
