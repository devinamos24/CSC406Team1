package edu.missouriwestern.csc406team1.database;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class SavingsAccount extends Account{

    // Non-null account number property
    @NotNull
    private long accountNumber;
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
        System.out.printf("Interest of %.2f added. New balance: %.2f%n", interest, newBalance);
    }
}
