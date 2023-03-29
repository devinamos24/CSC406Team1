package edu.missouriwestern.csc406team1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
   ArrayListFlow<Account> accounts_flow = new ArrayListFlow<>();
   ArrayListFlow<Loan> loans_flow = new ArrayListFlow<>();
     
   
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
   @NotNull
   private Double balance;//current balance of the account
   @NotNull
   private Date dateOpened;//date the account was opened with the bank
   @Nullable // The interest rate can be null if it is a basic checking account
   private Double interestRate;//current interest rate of the account
   @NotNull
   private String primaryKey;//unique value to identify the account in the database
   @NotNull
   private String foreignKey;//unique value(SSN) to identify the customer owning the account
   public Account(@NotNull Double balance, @NotNull Date dateOpened, @Nullable Double interestRate) {
      this.balance = balance;
      this.dateOpened = dateOpened;
      this.interestRate = interestRate;
   }

   @NotNull
   public Double getBalance() { return balance; }
   public void setBalance(@NotNull Double balance) { this.balance = balance; }

   @NotNull
   public Date getDateOpened() { return dateOpened; }
   public void setDateOpened(@NotNull Date dateOpened) { this.dateOpened = dateOpened; }

   @Nullable
   public Double getInterestRate() { return interestRate; }
   public void setInterestRate(@Nullable Double interestRate) { this.interestRate = interestRate; }
}

// Represents a savings account in the bank system.
class SavingsAccount extends Account {
   // Non-null account number property
   @NotNull private long accountNumber;
   // Non-null account holder name property
   @NotNull private String accountHolderName;

   // Constructor for the SavingsAccount class
   public SavingsAccount(@NotNull Double balance, @NotNull Date dateOpened, @NotNull Double interestRate) {
      // Call the constructor of the superclass (Account) with the provided balance, dateOpened, and interestRate
      super(balance, dateOpened, interestRate);
      // Initialize accountNumber and accountHolderName properties
      this.accountNumber = accountNumber;
      this.accountHolderName = accountHolderName;
   }

   // Getter method for accountNumber
   public long getAccountNumber() { return accountNumber; }

   // Setter method for accountNumber
   public void setAccountNumber(long accountNumber) { this.accountNumber = accountNumber;}

   // Getter method for accountHolderName with a non-null constraint
   @NotNull
   public String getAccountHolderName() { return accountHolderName; }

   // Setter method for accountHolderName with a non-null constraint
   public void setAccountHolderName(@NotNull String accountHolderName) { this.accountHolderName = accountHolderName; }

   // Method to deposit a given amount into the account
   public void deposit(double amount) {
      double newBalance = getBalance() + amount;
      setBalance(newBalance);
      // Check if the deposit amount is positive
      if (amount <= 0) {
         System.out.println("Invalid deposit amount. Please enter a positive value.");
      } else {
         // Update the balance and print a success message
         newBalance += amount;
         System.out.printf("Successfully deposited %.2f. New balance: %.2f%n", amount, newBalance);
      }
   }

   // Method to withdraw a given amount from the account
   public void withdraw(double amount) {
      double newBalance = getBalance() + amount;
      setBalance(newBalance);
      // Check if the withdrawal amount is positive
      if (amount <= 0) {
         System.out.println("Invalid withdrawal amount. Please enter a positive value.");
      } else if (amount > newBalance) {
         // Check if there are sufficient funds for the withdrawal
         System.out.println("Insufficient funds. Unable to withdraw.");
      } else {
         // Update the balance and print a success message
         newBalance -= amount;
         System.out.printf("Successfully withdrew %.2f. New balance: %.2f%n", amount, newBalance);
      }
   }

   // Method to add interest to the account based on the balance and interest rate
   public void addInterest() {
      double interest = getBalance() * (getInterestRate() / 100);
      double newBalance = getBalance() + interest;
      setBalance(newBalance);
   }
}

/**
 * Represents a certificate of deposit (CD) savings account in the bank system. 
 */
class CD extends SavingsAccount {

   @NotNull
   private Date dueDate;   //Date CD is due to complete
   @NotNull
   private Integer rolloverTimeframe;//how long until a CD will roll over after due date

   public CD(@NotNull Double balance, @NotNull Date dateOpened, @NotNull Double interestRate, @NotNull Date dueDate, @NotNull Integer rolloverTimeframe) {
      super(balance, dateOpened, interestRate);
      this.dueDate = dueDate;
      this.rolloverTimeframe = rolloverTimeframe;
   }

   @NotNull
   public Date getDueDate() { return dueDate; }
   public void setDueDate(@NotNull Date dueDate) { this.dueDate = dueDate; }

   @NotNull
   public Integer getRolloverTimeframe() { return rolloverTimeframe; }
   public void setRolloverTimeframe(@NotNull Integer rolloverTimeframe) { this.rolloverTimeframe = rolloverTimeframe; }
}

// Represents a checking account in the bank system.
abstract class CheckingAccount extends Account {
   @NotNull
   private Double transactionFee;//cost of fee per transaction
   @Nullable
   private SavingsAccount backupAccount;//pointer to the account designated as the backup account if setup (must be savings account)

   public CheckingAccount(@NotNull Double balance, @NotNull Date dateOpened, @Nullable Double interestRate, @NotNull Double transactionFee, @Nullable SavingsAccount backupAccount) {
      super(balance, dateOpened, interestRate);
      this.transactionFee = transactionFee;
      this.backupAccount = backupAccount;
   }

   @NotNull
   public Double getTransactionFee() { return transactionFee; }
   public void setTransactionFee(@NotNull Double transactionFee) { this.transactionFee = transactionFee; }

   @Nullable
   public SavingsAccount getBackupAccount() { return backupAccount; }
   public void setBackupAccount(@Nullable SavingsAccount backupAccount) { this.backupAccount = backupAccount; }

       
}

// Represents a "That's My Bank" (TMB) checking account in the bank system.
// TODO: implement a way of finding monthly transaction fees (in this case it is 1.25)
class TMBAccount extends CheckingAccount {

   private static final Double defaultTransactionFee = 0.75;

   //constructor
   public TMBAccount(@NotNull Double balance, @NotNull Date dateOpened, @NotNull SavingsAccount backupAccount) {
      super(balance, dateOpened, null, defaultTransactionFee, backupAccount);
   }

   public TMBAccount(@NotNull Double balance, @NotNull Date dateOpened) {
      super(balance, dateOpened, null, defaultTransactionFee, null);
   }
}

// Represents a gold checking account in the bank system.
class GoldDiamondAccount extends CheckingAccount {
   @NotNull
   private static final Double defaultMinimumBalance = 5000.0; //minimum balanced required to not pay transaction fees
   @NotNull
   private Double minimumBalance;

   //constructor
   public GoldDiamondAccount(@NotNull Double balance, @NotNull Date dateOpened, @NotNull Double interestRate ,@NotNull SavingsAccount backupAccount, @NotNull Double minimumBalance) {
      super(balance, dateOpened, interestRate, (balance >= minimumBalance) ? 0.0: 0.75, backupAccount);
      this.minimumBalance = minimumBalance;
   }

   public GoldDiamondAccount(@NotNull Double balance, @NotNull Date dateOpened, @NotNull Double interestRate, @NotNull SavingsAccount backupAccount) {
      super(balance, dateOpened, interestRate, (balance >= defaultMinimumBalance) ? 0.0: 0.75, backupAccount);
      this.minimumBalance = defaultMinimumBalance;
   }

   public GoldDiamondAccount(@NotNull Double balance, @NotNull Date dateOpened, @NotNull Double interestRate, @NotNull Double minimumBalance) {
      super(balance, dateOpened, interestRate, (balance >= minimumBalance) ? 0.0: 0.75, null);
      this.minimumBalance = minimumBalance;
   }

    // TODO: make a factory for the GoldDiamondAccount because of all the different combinations

   @NotNull
   public Double getMinimumBalance() { return minimumBalance; }
   public void setMinimumBalance(@NotNull Double minimumBalance) { this.minimumBalance = minimumBalance; }
}

// Represents a loan offered by the bank.
abstract class Loan {
   @NotNull
   private Date paymentDue; //date next payment is due
   @Nullable
   private Date paymentNotified; //date notification is sent for next payment
   @NotNull
   private Double currentPaymentDue; //amount due for next payment
   @NotNull
   private Date dateSinceLastPayment; //date last payment was recieved
   @NotNull
   private Boolean missedPayment; //flag if a payment was missed
   
   //Should Loan and account be a single object that the other types of accounts extend from?
   //NOTE: The other loan types can extend from the base loan class.
   //However, you must keep in mind that they need to customize some of their own functionality
   //The below attributes are also in account.
   @NotNull
   private Double interestRate;
   @NotNull
   private Double balance;//current balance of the account
   @NotNull
   private Date dateOpened;//date the account was opened with the bank

   public Loan(@NotNull Date paymentDue, @NotNull Double currentPaymentDue, @NotNull Date dateSinceLastPayment, @NotNull Double interestRate, @NotNull Double balance, @NotNull Date dateOpened) {
      this.paymentDue = paymentDue;
      this.paymentNotified = null;
      this.currentPaymentDue = currentPaymentDue;
      this.dateSinceLastPayment = dateSinceLastPayment;
      this.missedPayment = false;
      this.interestRate = interestRate;
      this.balance = balance;
      this.dateOpened = dateOpened;
   }

   @NotNull
   public Date getPaymentDue() { return paymentDue; }
   public void setPaymentDue(@NotNull Date paymentDue) { this.paymentDue = paymentDue; }

   @Nullable
   public Date getPaymentNotified() { return paymentNotified; }
   public void setPaymentNotified(@Nullable Date paymentNotified) { this.paymentNotified = paymentNotified; }

   @NotNull
   public Double getCurrentPaymentDue() { return currentPaymentDue; }
   public void setCurrentPaymentDue(@NotNull Double currentPaymentDue) { this.currentPaymentDue = currentPaymentDue; }

   @NotNull
   public Date getDateSinceLastPayment() { return dateSinceLastPayment; }
   public void setDateSinceLastPayment(@NotNull Date dateSinceLastPayment) { this.dateSinceLastPayment = dateSinceLastPayment; }

   @NotNull
   public Boolean hasMissedPayment() { return missedPayment; }
   public void setMissedPayment(@NotNull Boolean missedPayment) { this.missedPayment = missedPayment; }
}

// TODO: implement business logic for each loan type
// Represents a long-term mortgage loan offered by the bank.
class MortgageLoan extends Loan {
   public MortgageLoan(@NotNull Date paymentDue, @NotNull Double currentPaymentDue, @NotNull Date dateSinceLastPayment, @NotNull Double interestRate, @NotNull Double balance, @NotNull Date dateOpened) {
      super(paymentDue, currentPaymentDue, dateSinceLastPayment, interestRate, balance, dateOpened);
   }
}

// Represents a short-term loan offered by the bank.
class ShortTermLoan extends Loan {
   public ShortTermLoan(@NotNull Date paymentDue, @NotNull Double currentPaymentDue, @NotNull Date dateSinceLastPayment, @NotNull Double interestRate, @NotNull Double balance, @NotNull Date dateOpened) {
      super(paymentDue, currentPaymentDue, dateSinceLastPayment, interestRate, balance, dateOpened);
   }
}

// Represents a credit card loan offered by the bank.
class CreditCardLoan extends Loan {
   public CreditCardLoan(@NotNull Date paymentDue, @NotNull Double currentPaymentDue, @NotNull Date dateSinceLastPayment, @NotNull Double interestRate, @NotNull Double balance, @NotNull Date dateOpened) {
      super(paymentDue, currentPaymentDue, dateSinceLastPayment, interestRate, balance, dateOpened);
   }
}

// Represents an ATM card linked to an account.
class ATMCard {
   private String pinNumber; //access PIN to use card
}
