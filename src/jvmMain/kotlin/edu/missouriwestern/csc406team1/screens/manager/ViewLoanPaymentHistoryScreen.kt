package edu.missouriwestern.csc406team1.screens.manager

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.LoanRepository
import edu.missouriwestern.csc406team1.database.TransactionRepository
import edu.missouriwestern.csc406team1.database.model.Transaction
import edu.missouriwestern.csc406team1.util.*
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("MMM dd, uuuu")

@Composable
fun ManagerViewLoanPaymentHistoryScreen(
    customerRepository: CustomerRepository,
    loanRepository: LoanRepository,
    transactionRepository: TransactionRepository,
    ssn: String,
    id: String,
    onBack: () -> Unit
) {

    val customers by customerRepository.customers.collectAsState()
    val loans by loanRepository.loans.collectAsState()
    val transactions by transactionRepository.transactions.collectAsState()

    val customer = customers.find { it.ssn == ssn }
    val loan = loans.find { it.accountNumber == id }
    val loanTransactions = transactions.filter { it.accID == id && it.transactionType == "lp" }.sorted()
    val daysOfTransactions = loanTransactions.sorted().map { formatter.format(it.date)!! }.toSet()

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
                    onClick = onBack
                ) {
                    Text("Back")
                }
                if (customer != null && loan != null) {
                    Text(
                        text = "${customer.firstname} ${customer.lastname}"
                    )
                    Text(
                        text = loan.getName()
                    )
                }
            }

            if (customer != null && loan != null && loanTransactions.isNotEmpty()) {
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
                                LoanDateHeader(date = date)
                            }
                            loanTransactions.forEach { transaction ->
                                if (formatter.format(transaction.date) == date) {
                                    item {
                                        LoanTransactionButton(
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
        if (customer == null || loan == null) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "This loan or account no longer exists"
            )
        } else if (transactions.isEmpty()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "This loan has no payment history"
            )
        }
    }
}

@Composable
private fun LoanDateHeader(
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
private fun LoanTransactionButton(
    modifier: Modifier = Modifier,
    transaction: Transaction,
) {
    Box(
        modifier = modifier.height(48.dp)
            .fillMaxWidth()
            .padding(start = 10.dp)
    ) {
        val creditText = "Loan Payment"
        val modifierSymbol = "-"
        val color = Color.Green

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