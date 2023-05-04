package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.AccountRepository;
import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.LoanRepository;
import edu.missouriwestern.csc406team1.database.TransactionRepository;
import edu.missouriwestern.csc406team1.database.model.Check;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.database.model.Transaction;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import edu.missouriwestern.csc406team1.database.model.account.CheckingAccount;
import edu.missouriwestern.csc406team1.database.model.loan.CreditCardLoan;
import edu.missouriwestern.csc406team1.database.model.loan.Loan;
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
    private final LoanRepository loanRepository;
    private final TransactionRepository transactionRepository;
    private final MutableStateFlow<Boolean> hasFailed = StateFlowKt.MutableStateFlow(false);
    private final MutableStateFlow<String> hasFailedText = StateFlowKt.MutableStateFlow("Purchase Failed, try again");
    private final MutableStateFlow<InputWrapper> amount = StateFlowKt.MutableStateFlow(new InputWrapper());
    private final MutableStateFlow<Boolean> isAccount = StateFlowKt.MutableStateFlow(true);
    private final MutableStateFlow<String> selectedAccountId = StateFlowKt.MutableStateFlow("");
    private final ArrayListFlow<Check> unprocessedChecks = new ArrayListFlow<>();
    private final String ssn;
    private final Function0<Unit> onBack;

    public ShoppingScreenViewModel(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            LoanRepository loanRepository,
            TransactionRepository transactionRepository,
            String ssn,
            Function0<Unit> onBack
    ) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.loanRepository = loanRepository;
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

    public StateFlow<List<Loan>> getLoans() {
        return loanRepository.getLoans().getFlow();
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

    public StateFlow<Boolean> getIsAccount() {
        return FlowKt.asStateFlow(isAccount);
    }

    public void setIsAccount(Boolean isAccount) {
        this.isAccount.setValue(isAccount);
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

    public StateFlow<List<Check>> getUnprocessedChecks() {
        return unprocessedChecks.getFlow();
    }

    private int currentCheckNum = 1;
    public void onWriteCheck() {
        CheckingAccount account;
        double money;
        try {
            account = (CheckingAccount) accountRepository.getAccount(selectedAccountId.getValue());
            money = Double.parseDouble(amount.getValue().component1()) / 100;
        } catch (Exception e) {
            hasFailedText.setValue("Debiting account failed, try again");
            hasFailed.setValue(true);
            return;
        }

        Check check = new Check("", false, true, "ch", money, account.getBalance()-money, account.getAccountNumber(), LocalDate.now(), LocalTime.now(), currentCheckNum, null, false);
        currentCheckNum++;
        unprocessedChecks.add(check);
        transactionRepository.addTransaction(check);
    }

    public void onProcessChecks() {
        int failedChecks = 0;
        for (Check check : unprocessedChecks) {
            if (check.isStopPayment()) {
                unprocessedChecks.remove(check);
                failedChecks++;
                hasFailedText.setValue(failedChecks + " check(s) failed to process");
                hasFailed.setValue(true);
                continue;
            }
            CheckingAccount account;
            double money;
            try {
                account = (CheckingAccount) accountRepository.getAccount(check.getAccID());
                money = check.getAmount();
            } catch (Exception e) {
                unprocessedChecks.remove(check);
                failedChecks++;
                hasFailedText.setValue(failedChecks + " check(s) failed to process");
                hasFailed.setValue(true);
                return;
            }
            double fee = account.getTransactionFee();
            money += fee;
            if (account.getBalance() >= money) {
                account.setBalance(account.getBalance() - money);
                if (accountRepository.update(account)) {
                    check.setPayee("Walmart");
                    transactionRepository.update(check);
                    if (fee > 0.0) {
                        transactionRepository.addTransaction(new Transaction("", false, true, "f", fee, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                    }
                    unprocessedChecks.remove(check);
                    continue;
                }
            } else {
                if (account.getBackupAccount() != null && account.getBalance() + account.getBackupAccount().getBalance() >= money) {
                    account.getBackupAccount().setBalance(account.getBackupAccount().getBalance() - (money - account.getBalance()));
                    double oldBalance = account.getBalance();
                    account.setBalance(0.0);
                    if (accountRepository.update(account) && accountRepository.update(account.getBackupAccount())) {
                        transactionRepository.addTransaction(new Transaction("", false, true, "d", oldBalance, account.getBalance() + fee, account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                        transactionRepository.addTransaction(new Transaction("", false, true, "d", money - oldBalance, account.getBackupAccount().getBalance(), account.getBackupAccount().getAccountNumber(), LocalDate.now(), LocalTime.now()));
                        if (fee > 0.0) {
                            transactionRepository.addTransaction(new Transaction("", false, true, "f", fee, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                        }
                    } else {
                        failedChecks++;
                        hasFailedText.setValue(failedChecks + " check(s) failed to process");
                        hasFailed.setValue(true);
                        unprocessedChecks.remove(check);
                        continue;
                    }
                } else {
                    account.setBalance(account.getBalance() - 25.0);
                    account.setOverdraftsThisMonth(account.getOverdraftsThisMonth() + 1);
                    if (accountRepository.update(account)) {
                        transactionRepository.addTransaction(new Transaction("", false, true, "f", 25.0, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                    }
                    failedChecks++;
                    hasFailedText.setValue(failedChecks + " check(s) failed to process");
                    hasFailed.setValue(true);
                }
                unprocessedChecks.remove(check);
                continue;
            }
            unprocessedChecks.remove(check);
            failedChecks++;
            hasFailedText.setValue(failedChecks + " check(s) failed to process");
            hasFailed.setValue(true);
        }

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
        } catch (Exception e) {
            hasFailedText.setValue("Debiting account failed, try again");
            hasFailed.setValue(true);
            return;
        }
        double fee = account.getTransactionFee();
        money += fee;
        if (account.getBalance() >= money) {
            account.setBalance(account.getBalance() - money);
            if (accountRepository.update(account)) {
                transactionRepository.addTransaction(new Transaction("", false, true, "d", money, account.getBalance() + fee, account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                if (fee > 0.0) {
                    transactionRepository.addTransaction(new Transaction("", false, true, "f", fee, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                }
                hasFailed.setValue(false);
                return;
            }
        } else {
            if (account.getBackupAccount() != null && account.getBalance() + account.getBackupAccount().getBalance() >= money) {
                account.getBackupAccount().setBalance(account.getBackupAccount().getBalance() - (money - account.getBalance()));
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

    public void onMakePurchaseCreditCard() {
        CreditCardLoan creditCard;
        double money;
        try {
            creditCard = (CreditCardLoan) loanRepository.getLoan(selectedAccountId.getValue());
            money = Double.parseDouble(amount.getValue().component1()) / 100;
        } catch (NumberFormatException e) {
            hasFailedText.setValue("Debiting account failed, try again");
            hasFailed.setValue(true);
            return;
        }
        if (creditCard.getCreditLimit() - creditCard.getBalance() >= money) {
            creditCard.setBalance(creditCard.getBalance() + money);
            if (loanRepository.update(creditCard)) {
                transactionRepository.addTransaction(new Transaction("", false, true, "ccp", money, creditCard.getBalance(), creditCard.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                hasFailed.setValue(false);
                return;
            }
        } else {
            creditCard.setBalance(creditCard.getBalance() + 25.0);
            if (loanRepository.update(creditCard)) {
                transactionRepository.addTransaction(new Transaction("", false, true, "ccf", 25.0, creditCard.getBalance(), creditCard.getAccountNumber(), LocalDate.now(), LocalTime.now()));
            }
            hasFailedText.setValue("Insufficient credit available, applying over-the-limit fee");
            hasFailed.setValue(true);
            return;
        }
        hasFailedText.setValue("Debiting account failed, try again");
        hasFailed.setValue(true);
    }

    public void onBack() {
        onBack.invoke();
    }
}
