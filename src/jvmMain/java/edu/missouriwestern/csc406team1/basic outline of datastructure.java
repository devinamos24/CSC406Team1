package edu.missouriwestern.csc406team1;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

//datastructure basic outline for Banking System

// Represents a user of the system.
abstract class User {
   
   private String firstName;
   private String lastName;
   
}

// Represents a customer user of the system.
class Customer extends User {
   private String SSN;
   private String streetAdress;
   private String city;
   private String state;
   private String zipCode;
   private String accountNumber; //could be used in interface to "login" and pull up a customers accounts
     
   
}

// Represents a bank teller user of the system.
class BankTeller extends User {
   private String employeeID; //could be used in interface to "login" to bank teller menu
}

// Represents a bank manager user of the system.
class BankManager extends User {
   private String managerCode; //"access code" of sorts for the manager to login to system
}

// Represents an account in the bank system.
abstract class Account {
   private double balance;//current balance of the account
   private Date dateOpened;//date the account was opened with the bank
   private double interestRate;//current interest rate of the account

   public double getBalance() { return balance; }
   public void setBalance(double balance) { this.balance = balance; }

   public Date getDateOpened() { return dateOpened; }
   public void setDateOpened(Date dateOpened) { this.dateOpened = dateOpened; }

   public double getInterestRate() { return interestRate; }
   public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
}

// Represents a savings account in the bank system.
class SavingsAccount extends Account {
   
}

/**
 * Represents a certificate of deposit (CD) savings account in the bank system. 
 */
class CD extends SavingsAccount {
   
   private Date dueDate;   //Date CD is due to complete
   private int rolloverTimeframe;//how long until a CD will roll over after due date

   public Date getDueDate() { return dueDate; }
   public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

   public int getRolloverTimeframe() { return rolloverTimeframe; }
   public void setRolloverTimeframe(int rolloverTimeframe) { this.rolloverTimeframe = rolloverTimeframe; }
}

// Represents a checking account in the bank system.
class CheckingAccount extends Account {
   private double transactionFee;//cost of fee per transaction
   private boolean hasBackupAccount;//flag to indicate if a savings account is linked as a backup
   private Account backupAccount;//pointer to the account designated as the backup account if setup
   
   public double getTransactionFee() { return transactionFee; }
   public void setTransactionFee(double transactionFee) { this.transactionFee = transactionFee; }

       
}

// Represents a "That's My Bank" (TMB) checking account in the bank system.
class TMBAccount extends CheckingAccount {
   
   //constructor
   public TMBAccount() {
      setTransactionFee(1.25); //sets transaction fee to $1.25
   }
}

// Represents a gold checking account in the bank system.
class GoldDiamondAccount extends CheckingAccount {
   private double minimumBalance;//minimum balanced required to not pay transaction fees

   //constructor
   public GoldDiamondAccount() {
       minimumBalance = 5000; //sets minimum balance to $5,000
   }

   public double getMinimumBalance() { return minimumBalance; }
   public void setMinimumBalance(double minimumBalance) { this.minimumBalance = minimumBalance; }
}

// Represents a loan offered by the bank.
abstract class Loan {
   private Date paymentDue; //date next payment is due
   private Date paymentNotified; //date notification is sent for next payment
   private double currentPaymentDue; //amount due for next payment
   private Date dateSinceLastPayment; //date last payment was recieved
   private boolean missedPayment; //flag if a payment was missed
   
   //Should Loan and account be a single object that the other types of accounts extend from? 
   //The below attributes are also in account. 
   private double interestRate; 
   private double balance;//current balance of the account
   private Date dateOpened;//date the account was opened with the bank


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
   private String pinNumber; //access PIN to use card
}
