package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.database.AccountRepository;
import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.LoanRepository;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import edu.missouriwestern.csc406team1.database.model.loan.Loan;
import edu.missouriwestern.csc406team1.util.InputWrapper;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

import java.util.List;

public class SelectBankAccountScreenViewModel {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final LoanRepository loanRepository;
    private final MutableStateFlow<Boolean> hasFailed = StateFlowKt.MutableStateFlow(false);
    private final MutableStateFlow<InputWrapper> amount = StateFlowKt.MutableStateFlow(new InputWrapper());
    private final String ssn;
    private final Function2<String, String, Unit> onClickAccount;
    private final Function2<String, String, Unit> onClickLoan;
    private final Function0<Unit> onBack;

    public SelectBankAccountScreenViewModel(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            LoanRepository loanRepository,
            String ssn,
            Function2<String, String, Unit> onClickAccount,
            Function2<String, String, Unit> onClickLoan,
            Function0<Unit> onBack
    ) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.loanRepository = loanRepository;
        this.ssn = ssn;
        this.onClickAccount = onClickAccount;
        this.onClickLoan = onClickLoan;
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

    public void onClickAccount(String id) {
        onClickAccount.invoke(ssn, id);
    }

    public void onClickLoan(String id) {
        onClickAccount.invoke(ssn, id);
    }

    public void onBack() {
        onBack.invoke();
    }
}
