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
}

// Represents a savings account in the bank system.
class SavingsAccount extends Account {
}

// Represents a checking account in the bank system.
class CheckingAccount extends Account {
}

// Represents a "That's My Bank" (TMB) checking account in the bank system.
class TMBAccount extends CheckingAccount {
}

// Represents a gold checking account in the bank system.
class GoldAccount extends CheckingAccount {
}

// Represents a diamond checking account in the bank system.
class DiamondAccount extends CheckingAccount {
}

// Represents a loan offered by the bank.
abstract class Loan {
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
