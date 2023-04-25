package edu.missouriwestern.csc406team1.database.dao;


import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.model.loan.Loan;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LoanDao {
    /**
     * Adds a loan to the data store.
     *
     * @param loan The loan object to be added.
     * @return true if the loan is added successfully, false otherwise.
     */
    boolean addLoan(Loan loan);
    /**
     * Retrieves all loans from the data store.
     *
     * @return An ArrayListFlow containing all loans.
     */
    @NotNull
    ArrayListFlow<Loan> getLoans();
    /**
     * Retrieves a loan from the data store by its account number.
     *
     * @param accountNumber The account number of the loan to be retrieved.
     * @return The loan object if found, null otherwise.
     */
    @Nullable
    Loan getLoan(String accountNumber);//accountNumber is referencing the loan account
    /**
     * Updates a loan's information in the data store.
     *
     * @param loan The loan object containing the updated information.
     * @return true if the loan is updated successfully, false otherwise.
     */
    boolean updateLoan(Loan loan);
    /**
     * Deletes a loan from the data store by its account number.
     *
     * @param accountNumber The account number of the loan to be deleted.
     */
    void deleteLoan(String accountNumber);//accountNumber references the loan account to be deleted
    /**
     * Saves any changes made to the data store.
     *
     * @return true if the changes are saved successfully, false otherwise.
     */
    boolean save();
}
