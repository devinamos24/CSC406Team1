package edu.missouriwestern.csc406team1.screens.teller

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
import edu.missouriwestern.csc406team1.database.TransactionRepository
import edu.missouriwestern.csc406team1.database.model.account.CDAccount
import edu.missouriwestern.csc406team1.database.model.account.GoldDiamondAccount
import edu.missouriwestern.csc406team1.database.model.account.SavingsAccount
import edu.missouriwestern.csc406team1.database.model.account.TMBAccount
import edu.missouriwestern.csc406team1.util.DateConverter
import edu.missouriwestern.csc406team1.util.collectAsState
import edu.missouriwestern.csc406team1.util.formatAsMoney

@Composable
fun TellerEditCustomerBankAccountScreen(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    transactionRepository: TransactionRepository,
    ssn: String,
    id: String,
    onCreditAccount: (String, String) -> Unit,
    onDebitAccount: (String, String) -> Unit,
    onTransferMoney: (String, String) -> Unit,
    onBack: () -> Unit
) {

    val customers by customerRepository.customers.collectAsState()
    val accounts by accountRepository.accounts.collectAsState()
    val transactions by transactionRepository.transactions.collectAsState()

    val customer = customers.find { it.ssn == ssn }
    val account = accounts.find { it.accountNumber == id }
    val accountTransactions = transactions.filter { it.accID == id }

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

                if (customer != null) {
                    Button(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = {
                            accountRepository.delete(id)
                            accountTransactions.forEach {
                                transactionRepository.delete(it.transactionID)
                            }
                            onBack()
                        }
                    ) {
                        Text("Delete Account")
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
                            is TMBAccount -> TMBAccountDetails(detailsModifier, account)
                            is GoldDiamondAccount -> GoldDiamondAccountDetails(detailsModifier, account)
                            is CDAccount -> CDAccountDetails(detailsModifier, account)
                            is SavingsAccount -> SavingsAccountDetails(detailsModifier, account)
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize().weight(0.5f)
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center).width(IntrinsicSize.Max)
                        ) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onCreditAccount(ssn, id) },
                            ) {
                                Text("Credit Account")
                            }
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onDebitAccount(ssn, id) }
                            ) {
                                Text("Debit Account")
                            }
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onTransferMoney(ssn, id) }
                            ) {
                                Text ("Transfer Money")
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
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${DateConverter.convertDateToString(account.dateOpened)}")
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
    account: GoldDiamondAccount,
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${DateConverter.convertDateToString(account.dateOpened)}")
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
    account: SavingsAccount,
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${DateConverter.convertDateToString(account.dateOpened)}")
        Text("Current Balance: ${account.balance.formatAsMoney()}")
        Text("Daily Interest Rate: ${account.interestRate?.times(100)}%")
    }
}

@Composable
private fun CDAccountDetails(
    modifier: Modifier = Modifier,
    account: CDAccount,
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${DateConverter.convertDateToString(account.dateOpened)}")
        Text("Current Balance: ${account.balance.formatAsMoney()}")
        Text("Fixed Rate Of Return: ${account.interestRate?.times(100)}%")
        Text("Date Complete: ${DateConverter.convertDateToString(account.dueDate)}")
    }
}