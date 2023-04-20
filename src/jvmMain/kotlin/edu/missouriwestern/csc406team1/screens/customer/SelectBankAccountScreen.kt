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
import edu.missouriwestern.csc406team1.database.LoanRepository
import edu.missouriwestern.csc406team1.util.collectAsState
import edu.missouriwestern.csc406team1.util.formatAsMoney
import edu.missouriwestern.csc406team1.util.getName

@Composable
fun CustomerSelectBankAccountScreen(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    loanRepository: LoanRepository,
    ssn: String,
    onClickAccount: (String, String) -> Unit,
    onClickLoan: (String, String) -> Unit,
    onBack: () -> Unit
) {
    val customers by customerRepository.customers.collectAsState()
    val accounts by accountRepository.accounts.collectAsState()
    val loans by loanRepository.loans.collectAsState()

    val customer = customers.find { it.ssn == ssn }
    val customerAccounts = accounts.filter { it.customerSSN == customer?.ssn && it.isActive }
    val customerLoans = loans.filter { it.accountNumber == customer?.ssn && it.balance > 0 }

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
                        .weight(0.5f)
                ) {
                    val state = rememberLazyListState()

                    LazyColumn(Modifier.fillMaxSize().padding(end = 12.dp).align(Alignment.Center), state) {
                        customerAccounts.sorted().forEach { account ->
                            item {
                                CustomerAccountButton(
                                    name = account.getName(),
                                    balance = account.balance,
                                    onClick = { onClickAccount(ssn, account.accountNumber) }
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
            if (customer != null && customerLoans.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .padding(10.dp)
                        .weight(0.5f)
                ) {
                    val state = rememberLazyListState()

                    LazyColumn(Modifier.fillMaxSize().padding(end = 12.dp).align(Alignment.Center), state) {
                        customerLoans.sorted().forEach { loan ->
                            item {
                                LoanAccountButton(
                                    name = loan.getName(),
                                    balance = loan.balance,
                                    onClick = { onClickLoan(ssn, loan.accountNumber) }
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

@Composable
private fun LoanAccountButton(
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
            text = balance.formatAsMoney() + " Owed"
        )
    }
}