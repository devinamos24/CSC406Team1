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
    customerSSN: String,
    accountId: String,
    onTransfer: (String, String) -> Unit,
    onWithdraw: (String, String) -> Unit,
    onDeposit: (String, String) -> Unit,
    onBack: () -> Unit
) {
    val customers by customerRepository.customers.collectAsState()
    val accounts by accountRepository.accounts.collectAsState()

    val customer = customers.find { it.ssn == customerSSN }
    val account = accounts.find { it.accountNumber == accountId }

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text("Back")
        }
        if (customer != null && account != null) {
            val detailsModifier = Modifier.align(Alignment.Center)
            when (account) {
                is TMBAccount -> TMBAccountDetails(detailsModifier, account, { onTransfer(customerSSN, accountId) }, onWithdraw, onDeposit)
                is GoldDiamondAccount -> GoldDiamondAccountDetails(detailsModifier, account, { onTransfer(customerSSN, accountId) }, onWithdraw, onDeposit)
                is CDAccount -> CDAccountDetails(detailsModifier, account, { onTransfer(customerSSN, accountId) }, onWithdraw, onDeposit)
                is SavingsAccount -> SavingsAccountDetails(detailsModifier, account, { onTransfer(customerSSN, accountId) }, onWithdraw, onDeposit)
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
    account: TMBAccount,
    onTransfer: () -> Unit,
    onWithdraw: (String) -> Unit,
    onDeposit: (String) -> Unit
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
        Row (
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = onTransfer
            ) {
                Text(text = "Transfer")
            }
            Button(
                onClick = { onWithdraw(account.accountNumber) }
            ) {
                Text(text = "Withdraw")
            }
            Button(
                onClick = { onDeposit(account.accountNumber) }
            ) {
                Text("Deposit")
            }
        }
    }
}

@Composable
private fun GoldDiamondAccountDetails(
    modifier: Modifier = Modifier,
    account: GoldDiamondAccount,
    onTransfer: () -> Unit,
    onWithdraw: (String) -> Unit,
    onDeposit: (String) -> Unit
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
        Row (
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = onTransfer
            ) {
                Text(text = "Transfer")
            }
            Button(
                onClick = { onWithdraw(account.accountNumber) }
            ) {
                Text(text = "Withdraw")
            }
            Button(
                onClick = { onDeposit(account.accountNumber) }
            ) {
                Text("Deposit")
            }
        }
    }
}

@Composable
private fun SavingsAccountDetails(
    modifier: Modifier = Modifier,
    account: SavingsAccount,
    onTransfer: () -> Unit,
    onWithdraw: (String) -> Unit,
    onDeposit: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${convertDateToString(account.dateOpened)}")
        Text("Current Balance: ${account.balance.formatAsMoney()}")
        Text("Daily Interest Rate: ${account.interestRate?.times(100)}%")
        Row (
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = onTransfer
            ) {
                Text(text = "Transfer")
            }
            Button(
                onClick = { onWithdraw(account.accountNumber) }
            ) {
                Text(text = "Withdraw")
            }
            Button(
                onClick = { onDeposit(account.accountNumber) }
            ) {
                Text("Deposit")
            }
        }
    }
}

@Composable
private fun CDAccountDetails(
    modifier: Modifier = Modifier,
    account: CDAccount,
    onTransfer: () -> Unit,
    onWithdraw: (String) -> Unit,
    onDeposit: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${convertDateToString(account.dateOpened)}")
        Text("Current Balance: ${account.balance.formatAsMoney()}")
        Text("Fixed Rate Of Return: ${account.interestRate?.times(100)}%")
        Text("Date Complete: ${convertDateToString(account.dueDate)}")
        Row (
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = onTransfer
            ) {
                Text(text = "Transfer")
            }
            Button(
                onClick = { onWithdraw(account.accountNumber) }
            ) {
                Text(text = "Withdraw")
            }
            Button(
                onClick = { onDeposit(account.accountNumber) }
            ) {
                Text("Deposit")
            }
        }
    }
}