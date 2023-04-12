package edu.missouriwestern.csc406team1.screens.customer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.missouriwestern.csc406team1.database.model.account.CDAccount
import edu.missouriwestern.csc406team1.database.model.account.CheckingAccount
import edu.missouriwestern.csc406team1.util.DateConverter.convertDateToString
import edu.missouriwestern.csc406team1.util.formatAsMoney
import edu.missouriwestern.csc406team1.util.getName
import edu.missouriwestern.csc406team1.viewmodel.customer.BankAccountScreenViewModel

@Composable
fun CustomerBankAccountDetailsScreen(
    bankAccountScreenViewModel: BankAccountScreenViewModel,
) {
    val customers by bankAccountScreenViewModel.customers.collectAsState()
    val accounts by bankAccountScreenViewModel.accounts.collectAsState()

    val customer = customers.find { it.ssn == bankAccountScreenViewModel.ssn }
    val account = accounts.find { it.accountNumber == bankAccountScreenViewModel.id }

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Button(
                onClick = bankAccountScreenViewModel::onBack
            ) {
                Text("Back")
            }
            if (customer != null && account != null) {
                Text(
                    text = account.getName()
                )
            }
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
                            onClick = bankAccountScreenViewModel::onTransfer
                        ) {
                            Text(text = "Transfer")
                        }
                        Button(
                            onClick = bankAccountScreenViewModel::onWithdraw
                        ) {
                            Text(text = "Withdraw")
                        }
                        Button(
                            onClick = bankAccountScreenViewModel::onDeposit
                        ) {
                            Text("Deposit")
                        }
                    }
                    if (account is CheckingAccount) {
                        Button(
                            onClick = bankAccountScreenViewModel::onSelectBackupAccount
                        ) {
                            Text (text = "Select Backup Account")
                        }
                    }
                    Button(
                        onClick = bankAccountScreenViewModel::onViewTransactionHistory
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
