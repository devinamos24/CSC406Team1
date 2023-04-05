package edu.missouriwestern.csc406team1.screens.customer

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
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
import edu.missouriwestern.csc406team1.util.collectAsState
import edu.missouriwestern.csc406team1.util.formatAsMoney

@Composable
fun CustomerSelectBankAccountScreen(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    customerSSN: String,
    onClickAccount: (String, String) -> Unit,
    onBack: () -> Unit
) {
    val customers by customerRepository.customers.collectAsState()
    val accounts by accountRepository.accounts.collectAsState()

    val customer = customers.find { it.ssn == customerSSN }
    val customerAccounts = accounts.filter { it.customerSSN == customer?.ssn }

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(if (customer != null) "Logout" else "Back")
            }
            if (customer != null && customerAccounts.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .padding(10.dp)
                ) {
                    val state = rememberLazyListState()

                    LazyColumn(Modifier.fillMaxSize().padding(end = 12.dp).align(Alignment.Center), state) {
                        customerAccounts.sorted().forEachIndexed { i, account ->
                            item {
                                CustomerAccountButton(
                                    name = "Account $i",
                                    balance = account.balance,
                                    onClick = { onClickAccount(customerSSN, account.accountNumber) }
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
        if (customer == null) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "This account no longer exists"
            )
        } else if (accounts.isEmpty()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "No bank accounts available, please call the bank to set up your first account"
            )
        }
    }
}

@Composable
private fun CustomerAccountButton(
    modifier: Modifier = Modifier,
    name: String,
    balance: Double,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.height(32.dp)
            .fillMaxWidth()
            .padding(start = 10.dp)
            .clickable { onClick() }
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = name
        )
        Text(
            modifier = Modifier.align(Alignment.CenterEnd),
            text = balance.formatAsMoney()
        )
    }
}