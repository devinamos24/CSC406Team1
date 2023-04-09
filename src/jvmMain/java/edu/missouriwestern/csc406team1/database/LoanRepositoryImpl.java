package edu.missouriwestern.csc406team1.database;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.dao.LoanDao;
import edu.missouriwestern.csc406team1.database.dao.LoanDaoImpl;
import edu.missouriwestern.csc406team1.database.model.loan.Loan;

public class LoanRepositoryImpl implements LoanRepository {

    private final LoanDao loanDao;

    public LoanRepositoryImpl() {
        loanDao = new LoanDaoImpl();
    }

    @Override
    public Loan getLoan(String id) {
        return loanDao.getLoan(id);
    }

    @Override
    public ArrayListFlow<Loan> getLoans() {
        return loanDao.getLoans();
    }

    @Override
    public boolean addLoan(Loan loan) {
        return loanDao.addLoan(loan);
    }

    @Override
    public boolean update(Loan loan) {
        return loanDao.updateLoan(loan);
    }

    @Override
    public void delete(String id) {
        loanDao.deleteLoan(id);
    }

    @Override
    public boolean save() {
        return loanDao.save();
    }
}
