package edu.missouriwestern.csc406team1.database;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.loan.Loan;

public interface LoanRepository {
    Loan getLoan(String id);
    ArrayListFlow<Loan> getLoans();
    boolean addLoan(Loan loan);
    boolean update(Loan loan);
    void delete(String id);
    boolean save();
}
