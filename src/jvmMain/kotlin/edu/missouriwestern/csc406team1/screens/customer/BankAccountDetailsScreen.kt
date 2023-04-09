package edu.missouriwestern.csc406team1.screens.customer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.missouriwestern.csc406team1.database.AccountRepository
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.model.account.CDAccount
import edu.missouriwestern.csc406team1.database.model.account.CheckingAccount
import edu.missouriwestern.csc406team1.database.model.account.GoldDiamondAccount
import edu.missouriwestern.csc406team1.database.model.account.SavingsAccount
import edu.missouriwestern.csc406team1.database.model.account.TMBAccount
import edu.missouriwestern.csc406team1.util.DateConverter.convertDateToString
import edu.missouriwestern.csc406team1.util.collectAsState
import edu.missouriwestern.csc406team1.util.formatAsMoney

@Composable
fun CustomerBankAccountDetailsScreen(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    ssn: String,
    id: String,
    onTransfer: (String, String) -> Unit,
    onWithdraw: (String, String) -> Unit,
    onDeposit: (String, String) -> Unit,
    onViewTransactionHistory: (String, String) -> Unit,
    onSelectBackupAccount: (String, String) -> Unit,
    onBack: () -> Unit
) {
    val customers by customerRepository.customers.collectAsState()
    val accounts by accountRepository.accounts.collectAsState()

    val customer = customers.find { it.ssn == ssn }
    val account = accounts.find { it.accountNumber == id }

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text("Back")
        }
        if (customer != null && account != null && account.isActive) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {

                Text("Date Opened: ${convertDateToString(account.dateOpened)}")
                Text("Current Balance: ${account.balance.formatAsMoney()}")
                if (account is CheckingAccount) {
                    Text("Overdrafts This Month: ${account.overdraftsThisMonth}")
                }
                if (account !is CDAccount) {
                    account.interestRate?.run {
                        Text("Daily Interest Rate: ${this.times(100)}%")
                    }
                }
                if (account is CheckingAccount) {
                    account.backupAccount?.let {
                        Text("Backup Account Balance: ${it.balance.formatAsMoney()}")
                    } ?: run {
                        Text("Backup Account Not Selected")
                    }
                }
                if (account is CDAccount) {
                    account.interestRate?.run {
                        Text("Fixed Rate Of Return: ${this.times(100)}%")
                    }
                    Text("Date Complete: ${convertDateToString(account.dueDate)}")
                }

                Row (
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (account !is CDAccount) {
                        Button(
                            onClick = { onTransfer(ssn, id) }
                        ) {
                            Text(text = "Transfer")
                        }
                        Button(
                            onClick = { onWithdraw(ssn, id) }
                        ) {
                            Text(text = "Withdraw")
                        }
                        Button(
                            onClick = { onDeposit(ssn, id) }
                        ) {
                            Text("Deposit")
                        }
                    }
                    if (account is CheckingAccount) {
                        Button(
                            onClick = { onSelectBackupAccount(ssn, id) }
                        ) {
                            Text (text = "Select Backup Account")
                        }
                    }
                    Button(
                        onClick = { onViewTransactionHistory(ssn, id) }
                    ) {
                        Text("Transaction History")
                    }
                }
            }
        } else {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "This account no longer exists"
            )
        }
    }
}

@Composable
private fun TMBAccountDetails(
    modifier: Modifier = Modifier,
    account: TMBAccount
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${convertDateToString(account.dateOpened)}")
        Text("Current Balance: ${account.balance.formatAsMoney()}")
        Text("Overdrafts This Month: ${account.overdraftsThisMonth}")
        account.backupAccount?.let {
            Text("Backup Account Balance: ${it.balance.formatAsMoney()}")
        } ?: run {
            Text("Backup Account Not Selected")
        }

    }
}

@Composable
private fun GoldDiamondAccountDetails(
    modifier: Modifier = Modifier,
    account: GoldDiamondAccount
    ) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${convertDateToString(account.dateOpened)}")
        Text("Current Balance: ${account.balance.formatAsMoney()}")
        Text("Overdrafts This Month: ${account.overdraftsThisMonth}")
        Text("Daily Interest Rate: ${account.interestRate?.times(100)}%")
        account.backupAccount?.let {
            Text("Backup Account Balance: ${it.balance.formatAsMoney()}")
        } ?: run {
            Text("Backup Account Not Selected")
        }
    }
}

@Composable
private fun SavingsAccountDetails(
    modifier: Modifier = Modifier,
    account: SavingsAccount
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${convertDateToString(account.dateOpened)}")
        Text("Current Balance: ${account.balance.formatAsMoney()}")
        Text("Daily Interest Rate: ${account.interestRate?.times(100)}%")
    }
}

@Composable
private fun CDAccountDetails(
    modifier: Modifier = Modifier,
    account: CDAccount
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${convertDateToString(account.dateOpened)}")
        Text("Current Balance: ${account.balance.formatAsMoney()}")
        Text("Fixed Rate Of Return: ${account.interestRate?.times(100)}%")
        Text("Date Complete: ${convertDateToString(account.dueDate)}")
    }
}
