package edu.missouriwestern.csc406team1.database.dao;


import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.loan.Loan;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LoanDao {
    void addLoan(Loan loan);

    @NotNull
    ArrayListFlow<Loan> getLoans();

    @Nullable
    Loan getLoan(String accountNumber);//accountNumber is referencing the loan account

    void updateLoan(Loan loan);
    void deleteLoan(String accountNumber);//accountNumber references the loan account to be deleted
    boolean save();
}
