package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.database.AccountRepository;
import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.LoanRepository;
import edu.missouriwestern.csc406team1.database.TransactionRepository;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.database.model.Transaction;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import edu.missouriwestern.csc406team1.database.model.account.GoldDiamondAccount;
import edu.missouriwestern.csc406team1.database.model.account.SavingsAccount;
import edu.missouriwestern.csc406team1.database.model.account.TMBAccount;
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
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * ViewModel class to handle the logic for the LoanMakePaymentScreen.
 */
public class LoanMakePaymentScreenViewModel {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final LoanRepository loanRepository;
    private final MutableStateFlow<Boolean> hasFailed = StateFlowKt.MutableStateFlow(false);
    private final MutableStateFlow<InputWrapper> amount = StateFlowKt.MutableStateFlow(new InputWrapper());
    private final MutableStateFlow<String> selectedAccountId = StateFlowKt.MutableStateFlow("");
    private final String ssn;
    private final String id;
    private final Function0<Unit> onBack;

    /**
     * Constructor for the LoanMakePaymentScreenViewModel.
     *
     * @param accountRepository     Repository for managing account data
     * @param customerRepository    Repository for managing customer data
     * @param transactionRepository Repository for managing transaction data
     * @param loanRepository        Repository for managing loan data
     * @param ssn                   Social security number of the customer
     * @param id                    Identifier for the loan
     */
    public LoanMakePaymentScreenViewModel(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            LoanRepository loanRepository,
            String ssn,
            String id,
            Function0<Unit> onBack
    ) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.loanRepository = loanRepository;
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

    public StateFlow<List<Loan>> getLoans() {
        return loanRepository.getLoans().getFlow();
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
        int accountBalance = Double.valueOf(accountRepository.getAccount(selectedAccountId.getValue()).getBalance() * 100).intValue();
        int loanBalance = Double.valueOf(loanRepository.getLoan(id).getBalance() * 100).intValue();
        if (accountBalance > loanBalance) {
            amount.setValue(new InputWrapper(String.valueOf(loanBalance), null));
        } else {
            amount.setValue(new InputWrapper(String.valueOf(accountBalance), null));
        }
    }

    public void onClickNone() {
        amount.setValue(new InputWrapper());
    }

    public void onAmountChange(String newAmount) {
        newAmount = newAmount.replaceAll("\\D", "");
        amount.setValue(new InputWrapper(newAmount, null));
    }

    /**
     * Makes a payment towards the given loan from the selected account.
     */
    public void onPay() {
        Account account = accountRepository.getAccount(selectedAccountId.getValue());
        Loan loan = loanRepository.getLoan(id);
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

        double savedLoanBalance = loan.getBalance();
        double savedAccountBalance = account.getBalance();

        loan.setBalance(loan.getBalance() - money);
        account.setBalance(account.getBalance() - money - fee);

        if (loanRepository.update(loan) && accountRepository.update(account)) {
            //TODO: Implement loans into transactionRepository
            transactionRepository.addTransaction(new Transaction("", false, true, "lp", money, loan.getBalance(), loan.getAccountNumber(), LocalDate.now(), LocalTime.now()));
            transactionRepository.addTransaction(new Transaction("", false, true, "lp", money, account.getBalance() + fee, account.getAccountNumber(), LocalDate.now(), LocalTime.now()));
            if (fee > 0.0) {
                transactionRepository.addTransaction(new Transaction("", false, true, "f", fee, account.getBalance(), account.getAccountNumber(), LocalDate.now(), LocalTime.now().plus(1, ChronoUnit.NANOS)));
            }
        } else {
            loan.setBalance(savedLoanBalance);
            account.setBalance(savedAccountBalance);
            loanRepository.update(loan);
            accountRepository.update(account);
            hasFailed.setValue(true);
        }
    }

    public void onBack() {
        onBack.invoke();
    }
}
