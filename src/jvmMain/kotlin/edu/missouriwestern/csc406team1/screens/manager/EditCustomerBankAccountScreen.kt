package edu.missouriwestern.csc406team1.screens.manager

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import edu.missouriwestern.csc406team1.database.AccountRepository
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.TransactionRepository
import edu.missouriwestern.csc406team1.database.model.account.CDAccount
import edu.missouriwestern.csc406team1.database.model.account.GoldDiamondAccount
import edu.missouriwestern.csc406team1.database.model.account.SavingsAccount
import edu.missouriwestern.csc406team1.database.model.account.TMBAccount
import edu.missouriwestern.csc406team1.util.*

@Composable
fun WindowScope.ManagerEditCustomerBankAccountScreen(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    transactionRepository: TransactionRepository,
    ssn: String,
    id: String,
    onCreditAccount: (String, String) -> Unit,
    onDebitAccount: (String, String) -> Unit,
    onTransfer: (String, String) -> Unit,
    onModifyInterest: (String, String) -> Unit,
    onViewTransactions: (String, String) -> Unit,
    onDeleteAccount: (String, String) -> Unit,
    onBack: () -> Unit
) {

    val customers by customerRepository.customers.collectAsState()
    val accounts by accountRepository.accounts.collectAsState()

    val customer = customers.find { it.ssn == ssn }
    val account = accounts.find { it.accountNumber == id }

    var cdWarningCredit by remember { mutableStateOf(false) }
    var cdWarningDebit by remember { mutableStateOf(false) }
    var cdWarningTransfer by remember { mutableStateOf(false) }




    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Column {

            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth()
            ) {

                Button(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text("Back")
                }

                if (customer != null && account != null && account.isActive) {
                    Button(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = { onDeleteAccount(ssn, id) }
                    ) {
                        Text("Close Account")
                    }
                }
            }
            if (customer != null && account != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().weight(0.5f)
                    ) {
                        val detailsModifier = Modifier.align(Alignment.Center)
                        when (account) {
                            is TMBAccount -> TMBAccountDetails(detailsModifier, account, account.isActive)
                            is GoldDiamondAccount -> GoldDiamondAccountDetails(detailsModifier, account, account.isActive)
                            is CDAccount -> CDAccountDetails(detailsModifier, account, account.isActive)
                            is SavingsAccount -> SavingsAccountDetails(detailsModifier, account, account.isActive)
                        }
                    }


                    Box(
                        modifier = Modifier.fillMaxSize().weight(0.5f)
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center).width(IntrinsicSize.Max)
                        ) {
                            if (account.isActive) {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { if (account !is CDAccount) onCreditAccount(ssn, id) else cdWarningCredit = true },
                                ) {
                                    Text("Credit Account")
                                }
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { if (account !is CDAccount) onDebitAccount(ssn, id) else cdWarningDebit = true }
                                ) {
                                    Text("Debit Account")
                                }
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { if (account !is CDAccount) onTransfer(ssn, id) else cdWarningTransfer = true }
                                ) {
                                    Text("Transfer")
                                }
                                if (account is GoldDiamondAccount || account is SavingsAccount && account !is CDAccount) {
                                    Button(
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { onModifyInterest(ssn, id) }
                                    ) {
                                        Text("Modify Interest")
                                    }
                                }
                            }
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onViewTransactions(ssn, id) }
                            ) {
                                Text("View Transaction History")
                            }
                        }
                    }
                }

                if (cdWarningCredit) {
                    CDWarning {
                        when (it) {
                            AlertDialogResult.Yes -> {
                                accountRepository.update(SavingsAccount(account.accountNumber, account.customerSSN, account.balance, account.dateOpened, account.isActive, account.interestRate!!))
                                onCreditAccount(ssn, id)
                            }
                            else -> {
                                cdWarningCredit = false
                            }
                        }
                    }
                }

                if (cdWarningDebit) {
                    CDWarning {
                        when (it) {
                            AlertDialogResult.Yes -> {
                                accountRepository.update(SavingsAccount(account.accountNumber, account.customerSSN, account.balance, account.dateOpened, account.isActive, account.interestRate!!))
                                onDebitAccount(ssn, id)
                            }
                            else -> {
                                cdWarningDebit = false
                            }
                        }
                    }
                }

                if (cdWarningTransfer) {
                    CDWarning {
                        when (it) {
                            AlertDialogResult.Yes -> {
                                accountRepository.update(SavingsAccount(account.accountNumber, account.customerSSN, account.balance, account.dateOpened, account.isActive, account.interestRate!!))
                                onTransfer(ssn, id)
                            }
                            else -> {
                                cdWarningTransfer = false
                            }
                        }
                    }
                }

            }
        }
        if (customer == null || account == null) {
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
    isActive: Boolean
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${DateConverter.convertDateToString(account.dateOpened)}")
        if (isActive) {
            Text("Current Balance: ${account.balance.formatAsMoney()}")
            Text("Overdrafts This Month: ${account.overdraftsThisMonth}")
            account.backupAccount?.let {
                Text("Backup Account Balance: ${it.balance.formatAsMoney()}")
            } ?: run {
                Text("Backup Account Not Selected")
            }
        } else {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.error,
                text = "Closed"
            )
        }
    }
}

@Composable
private fun GoldDiamondAccountDetails(
    modifier: Modifier = Modifier,
    account: GoldDiamondAccount,
    isActive: Boolean
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${DateConverter.convertDateToString(account.dateOpened)}")
        if (isActive) {
            Text("Current Balance: ${account.balance.formatAsMoney()}")
            Text("Overdrafts This Month: ${account.overdraftsThisMonth}")
            Text("Daily Interest Rate: ${account.interestRate?.times(100)}%")
            account.backupAccount?.let {
                Text("Backup Account Balance: ${it.balance.formatAsMoney()}")
            } ?: run {
                Text("Backup Account Not Selected")
            }
        } else {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.error,
                text = "Closed"
            )
        }
    }
}

@Composable
private fun SavingsAccountDetails(
    modifier: Modifier = Modifier,
    account: SavingsAccount,
    isActive: Boolean
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${DateConverter.convertDateToString(account.dateOpened)}")
        if (isActive) {
            Text("Current Balance: ${account.balance.formatAsMoney()}")
            Text("Daily Interest Rate: ${account.interestRate?.times(100)}%")
        } else {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.error,
                text = "Closed"
            )
        }
    }
}

@Composable
private fun CDAccountDetails(
    modifier: Modifier = Modifier,
    account: CDAccount,
    isActive: Boolean
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${DateConverter.convertDateToString(account.dateOpened)}")
        if (isActive) {
            Text("Current Balance: ${account.balance.formatAsMoney()}")
            Text("Fixed Rate Of Return: ${account.interestRate?.times(100)}%")
            Text("Date Complete: ${DateConverter.convertDateToString(account.dueDate)}")
        } else {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.error,
                text = "Closed"
            )
        }
    }
}

@Composable
fun WindowScope.CDWarning(
    onResult: (result: AlertDialogResult) -> Unit
) {
    YesNoCancelDialog(
        "Cancel CD Account",
        "This action will convert the CD Account into a standard savings. Are you sure you want to do this?"
        , onResult
    )
}