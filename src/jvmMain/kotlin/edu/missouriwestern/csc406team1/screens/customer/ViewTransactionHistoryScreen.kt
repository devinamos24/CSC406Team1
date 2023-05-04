package edu.missouriwestern.csc406team1.screens.customer

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.missouriwestern.csc406team1.database.model.Check
import edu.missouriwestern.csc406team1.database.model.Transaction
import edu.missouriwestern.csc406team1.util.DateConverter
import edu.missouriwestern.csc406team1.util.TimeConverter
import edu.missouriwestern.csc406team1.util.formatAsMoney
import edu.missouriwestern.csc406team1.util.getName
import edu.missouriwestern.csc406team1.viewmodel.customer.ViewTransactionHistoryScreenViewModel
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("MMM dd, uuuu")

@Composable
fun CustomerViewTransactionHistoryScreen(
    viewTransactionHistoryScreenViewModel: ViewTransactionHistoryScreenViewModel,
) {

    val customers by viewTransactionHistoryScreenViewModel.customers.collectAsState()
    val accounts by viewTransactionHistoryScreenViewModel.accounts.collectAsState()
    val transactions by viewTransactionHistoryScreenViewModel.transactions.collectAsState()

    val ssn = viewTransactionHistoryScreenViewModel.ssn
    val id = viewTransactionHistoryScreenViewModel.id

    val customer = customers.find { it.ssn == ssn }
    val account = accounts.find { it.accountNumber == id }
    val accountTransactions = transactions.filter { it.accID == id && (it.transactionType != "lp" || it.transactionType != "ccp" || it.transactionType != "ccf") && !(it is Check && it.payee == null) }.sorted()
    val daysOfTransactions = accountTransactions.sorted().map { formatter.format(it.date)!! }.toSet()

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Button(
                    onClick = viewTransactionHistoryScreenViewModel::onBack
                ) {
                    Text("Back")
                }
                if (customer != null && account != null) {
                    Text(
                        text = account.getName()
                    )
                }
            }

            if (customer != null && account != null && accountTransactions.isNotEmpty() && account.isActive) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .padding(10.dp)
                ) {
                    val state = rememberLazyListState()
                    LazyColumn(Modifier.fillMaxSize().padding(end = 12.dp).align(Alignment.Center), state) {
                        item {
                            Divider()
                        }
                        daysOfTransactions.forEach { date ->
                            item {
                                AccountDateHeader(date = date)
                            }
                            accountTransactions.forEach { transaction ->
                                if (formatter.format(transaction.date) == date) {
                                    item {
                                        AccountTransactionButton(
                                            transaction = transaction,
                                        )
                                        Spacer(
                                            modifier = Modifier.height(16.dp)
                                        )
                                    }
                                }
                            }
                            item {
                                Divider()
                            }
                        }
                    }
                    VerticalScrollbar(
                        modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                        adapter = rememberScrollbarAdapter(
                            scrollState = state
                        )
                    )
                }
            }
        }
        if (customer == null || account == null) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "This account no longer exists"
            )
        } else if (transactions.isEmpty()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "This account has no transaction history"
            )
        }
    }
}

@Composable
private fun AccountDateHeader(
    modifier: Modifier = Modifier,
    date: String
) {
    Box(
        modifier = modifier.height(32.dp)
            .fillMaxWidth()
            .padding(start = 10.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = date,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun AccountTransactionButton(
    modifier: Modifier = Modifier,
    transaction: Transaction,
) {
    Box(
        modifier = modifier.height(48.dp)
            .fillMaxWidth()
            .padding(start = 10.dp)
    ) {
        var creditText = if (transaction.isCredit) "Credit" else "Debit"
        val modifierSymbol = if (transaction.isCredit) "" else "-"
        val color = if (transaction.isCredit) Color.Green else Color.Unspecified

        when (transaction.transactionType) {
            "f" -> creditText = "Fee"
            "t" -> creditText = "Transfer"
            "ch" -> creditText = "Check"
        }

        Column(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "$modifierSymbol${transaction.amount.formatAsMoney()}",
                color = color,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
            Text(
                text = transaction.newTotal.formatAsMoney(),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        }

        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = "$creditText on ${DateConverter.convertDateToString(transaction.date)} at ${
                TimeConverter.convertTimeToString(
                    transaction.time
                )
            }",
        )
    }
}