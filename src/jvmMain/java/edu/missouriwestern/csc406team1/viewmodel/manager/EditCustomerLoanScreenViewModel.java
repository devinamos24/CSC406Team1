package edu.missouriwestern.csc406team1.viewmodel.manager;

import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.LoanRepository;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.database.model.loan.Loan;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.flow.StateFlow;

import java.util.List;

public class EditCustomerLoanScreenViewModel {
    private final CustomerRepository customerRepository;
    private final LoanRepository loanRepository;
    private final String ssn;
    private final String id;
    private final Function2<String, String, Unit> onCredit;
    private final Function2<String, String, Unit> onViewTransactionHistory;
    private final Function0<Unit> onBack;

    /**
     * Constructor for the LoanDetailsScreenViewModel.
     *
     * @param customerRepository Repository for managing customer data
     * @param loanRepository     Repository for managing loan data
     * @param ssn                Social security number of the customer
     * @param id                 Identifier for the loan
     */
    public EditCustomerLoanScreenViewModel(
            CustomerRepository customerRepository,
            LoanRepository loanRepository,
            String ssn,
            String id,
            Function2<String, String, Unit> onCredit,
            Function2<String, String, Unit> onViewTransactionHistory,
            Function0<Unit> onBack
    ) {
        this.customerRepository = customerRepository;
        this.loanRepository = loanRepository;
        this.ssn = ssn;
        this.id = id;
        this.onCredit = onCredit;
        this.onViewTransactionHistory = onViewTransactionHistory;
        this.onBack = onBack;
    }

    public StateFlow<List<Customer>> getCustomers() {
        return customerRepository.getCustomers().getFlow();
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

    public void onCredit() {
        onCredit.invoke(ssn, id);
    }

    public void onReleaseLien() {
        Loan loan = loanRepository.getLoan(id).copy();
        loan.setBalance(0.0);
        loanRepository.update(loan);
        onBack.invoke();
    }

    public void onViewTransactionHistory() {
        onViewTransactionHistory.invoke(ssn, id);
    }

    public void onBack() {
        onBack.invoke();
    }
}
