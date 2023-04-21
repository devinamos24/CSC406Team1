package edu.missouriwestern.csc406team1.screens.customer

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
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
import edu.missouriwestern.csc406team1.database.model.Transaction
import edu.missouriwestern.csc406team1.util.DateConverter
import edu.missouriwestern.csc406team1.util.collectAsState
import edu.missouriwestern.csc406team1.util.formatAsMoney

@Composable
fun CustomerViewTransactionHistoryScreen(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    transactionRepository: TransactionRepository,
    ssn: String,
    id: String,
    onBack: () -> Unit
) {

    val customers by customerRepository.customers.collectAsState()
    val accounts by accountRepository.accounts.collectAsState()
    val transactions by transactionRepository.transactions.collectAsState()

    val customer = customers.find { it.ssn == ssn }
    val account = accounts.find { it.accountNumber == id }
    val accountTransactions = transactions.filter { it.accID == id }.sorted()

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("Back")
            }

            if (customer != null && account != null && accountTransactions.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .padding(10.dp)
                ) {
                    val state = rememberLazyListState()
                    LazyColumn(Modifier.fillMaxSize().padding(end = 12.dp).align(Alignment.Center), state) {
                        accountTransactions.forEach { transaction ->
                            item {
                                AccountTransactionButton(
                                    transaction = transaction,
                                    onClick = {}
                                )
                                Spacer(modifier = Modifier.height(5.dp))
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
fun AccountTransactionButton(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.height(32.dp)
            .fillMaxWidth()
            .padding(start = 10.dp)
//            .clickable { onClick() }
    ) {
        val creditText = if (transaction.isCredit) "Credit" else "Debit"
        val modifierSymbol = if (transaction.isCredit) "+" else "-"
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = "$creditText $modifierSymbol${transaction.amount.formatAsMoney()} on ${DateConverter.convertDateToString(transaction.date)} at ${transaction.time}"
        )

    }
}