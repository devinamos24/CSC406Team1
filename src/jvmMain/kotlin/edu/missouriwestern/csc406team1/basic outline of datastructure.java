package edu.missouriwestern.csc406team1;
//datastructure basic outline for Banking System

// Represents a user of the system.
abstract class User {
}

// Represents a customer user of the system.
class Customer extends User {
}

// Represents a bank teller user of the system.
class BankTeller extends User {
}

// Represents a bank manager user of the system.
class BankManager extends User {
}

// Represents an account in the bank system.
abstract class Account {
   private double balance;
   private Date dateOpened;
   private double interestRate;

   public double getBalance() { return balance; }
   public void setBalance(double balance) { this.balance = balance; }

   public Date getDateOpened() { return dateOpened; }
   public void setDateOpened(Date dateOpened) { this.dateOpened = dateOpened; }

   public double getInterestRate() { return interestRate; }
   public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
}

// Represents a savings account in the bank system.
class SavingsAccount extends Account {
   private double interestRate;
}

/**
 * Represents a certificate of deposit (CD) savings account in the bank system.
 */
class CD extends SavingsAccount {
   
   private Date dueDate;
   private int rolloverTimeframe;

   public Date getDueDate() { return dueDate; }
   public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

   public int getRolloverTimeframe() { return rolloverTimeframe; }
   public void setRolloverTimeframe(int rolloverTimeframe) { this.rolloverTimeframe = rolloverTimeframe; }
}

// Represents a checking account in the bank system.
class CheckingAccount extends Account {
   private double transactionFee;
   private boolean backupAccount;

   public double getTransactionFee() { return transactionFee; }
   public void setTransactionFee(double transactionFee) { this.transactionFee = transactionFee; }

   public boolean hasBackupAccount() { return backupAccount; }
   public void setBackupAccount(boolean backupAccount) { this.backupAccount = backupAccount; }
    
}

// Represents a "That's My Bank" (TMB) checking account in the bank system.
class TMBAccount extends CheckingAccount {
   public TMBAccount() {
      setTransactionFee(1.25);
   }
}

// Represents a gold checking account in the bank system.
class GoldDiamondAccount extends CheckingAccount {
   private double minimumBalance;

   public GoldDiamondAccount() {
       minimumBalance = 5000;
   }

   public double getMinimumBalance() { return minimumBalance; }
   public void setMinimumBalance(double minimumBalance) { this.minimumBalance = minimumBalance; }
}

// Represents a loan offered by the bank.
abstract class Loan {
   private Date paymentDue;
   private Date paymentNotified;
   private double currentPaymentDue;
   private Date dateSinceLastPayment;
   private boolean missedPayment;

   public Date getPaymentDue() { return paymentDue; }
   public void setPaymentDue(Date paymentDue) { this.paymentDue = paymentDue; }

   public Date getPaymentNotified() { return paymentNotified; }
   public void setPaymentNotified(Date paymentNotified) { this.paymentNotified = paymentNotified; }

   public double getCurrentPaymentDue() { return currentPaymentDue; }
   public void setCurrentPaymentDue(double currentPaymentDue) { this.currentPaymentDue = currentPaymentDue; }

   public Date getDateSinceLastPayment() { return dateSinceLastPayment; }
   public void setDateSinceLastPayment(Date dateSinceLastPayment) { this.dateSinceLastPayment = dateSinceLastPayment; }

   public boolean hasMissedPayment() { return missedPayment; }
   public void setMissedPayment(boolean missedPayment) { this.missedPayment = missedPayment; }
}

// Represents a long-term mortgage loan offered by the bank.
class MortgageLoan extends Loan {
}

// Represents a short-term loan offered by the bank.
class ShortTermLoan extends Loan {
}

// Represents a credit card loan offered by the bank.
class CreditCardLoan extends Loan {
}

// Represents an ATM card linked to an account.
class ATMCard {
}
