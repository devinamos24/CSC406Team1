package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.database.AccountRepository;
import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.database.LoanRepository;
import edu.missouriwestern.csc406team1.database.TransactionRepository;
import edu.missouriwestern.csc406team1.database.model.Transaction;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import edu.missouriwestern.csc406team1.database.model.loan.Loan;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * ViewModel class to handle the logic for the LoanMakePaymentScreen.
 */
public class LoanMakePaymentScreenViewModel {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final LoanRepository loanRepository;
    private final String ssn;
    private final String id;

    /**
     * Constructor for the LoanMakePaymentScreenViewModel.
     *
     * @param accountRepository    Repository for managing account data
     * @param customerRepository    Repository for managing customer data
     * @param transactionRepository Repository for managing transaction data
     * @param loanRepository        Repository for managing loan data
     * @param ssn                   Social security number of the customer
     * @param id                    Identifier for the loan
     */
    public LoanMakePaymentScreenViewModel(AccountRepository accountRepository, CustomerRepository customerRepository,
                                          TransactionRepository transactionRepository, LoanRepository loanRepository,
                                          String ssn, String id) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
        this.loanRepository = loanRepository;
        this.ssn = ssn;
        this.id = id;
    }

    /**
     * Makes a payment towards the given loan from the selected account.
     *
     * @param amount         The amount to be paid
     * @param loan           The loan object to be updated
     * @param selectedAccount The account object to be updated
     * @return true if the payment fails, false otherwise
     */
    public boolean makePayment(double amount, Loan loan, Account selectedAccount) {
        boolean hasFailed = false;
        try {
            double money = amount / 100;
            loan.setBalance(loan.getBalance() - money);
            selectedAccount.setBalance(selectedAccount.getBalance() - money);

            if (loanRepository.update(loan) && accountRepository.update(selectedAccount)) {
                //TODO: Implement loans into transactionRepository
                //transactionRepository.addTransaction(new Transaction("", false, true, "p", money, loan.getBalance(), loan.getAccountNumber(), LocalDate.now(), LocalTime.now()));
                transactionRepository.addTransaction(new Transaction("", true, false, "t", money, selectedAccount.getBalance(), selectedAccount.getAccountNumber(), LocalDate.now(), LocalTime.now()));
            } else {
                hasFailed = true;
            }
        } catch (NumberFormatException e) {
            hasFailed = true;
        }
        return hasFailed;
    }
}
