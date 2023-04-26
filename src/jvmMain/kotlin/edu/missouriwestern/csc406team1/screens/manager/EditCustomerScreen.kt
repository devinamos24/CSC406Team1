package edu.missouriwestern.csc406team1.screens.manager

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.missouriwestern.csc406team1.database.AccountRepository
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.LoanRepository
import edu.missouriwestern.csc406team1.util.collectAsState
import edu.missouriwestern.csc406team1.util.formatAsMoney
import edu.missouriwestern.csc406team1.util.getName

@Composable
fun ManagerEditCustomerScreen(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    loanRepository: LoanRepository,
    ssn: String,
    onClickAccount: (String, String) -> Unit,
    onClickLoan: (String, String) -> Unit,
    onClickOpenAccount: (String) -> Unit,
    onClickOpenLoan: (String) -> Unit,
    onBack: () -> Unit
) {
    val customers by customerRepository.customers.collectAsState()
    val accounts by accountRepository.accounts.collectAsState()
    val loans by loanRepository.loans.collectAsState()

    val customer = customers.find { it.ssn == ssn }
    val customerAccounts = accounts.filter { it.customerSSN == ssn }.sorted()
    val customerLoans = loans.filter { it.customerSSN == customer?.ssn }

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterStart),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Button(
                        onClick = onBack
                    ) {
                        Text("Back")
                    }
                    if (customer != null) {
                        Text(
                            text = "${customer.firstname} ${customer.lastname}"
                        )
                    }
                }

                if (customer != null) {
                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Button(
                            modifier = Modifier,
                            onClick = {
                                onClickOpenAccount(ssn)
                            }
                        ) {
                            Text("Open New Account")
                        }

                        Button(
                            modifier = Modifier,
                            onClick = {
                                onClickOpenLoan(ssn)
                            }
                        ) {
                            Text("Open New Loan")
                        }

                        Button(
                            modifier = Modifier,
                            onClick = {
                                customerRepository.delete(ssn)
                                customerAccounts.forEach {
                                    accountRepository.delete(it.accountNumber)
                                }
                                onBack()
                            }
                        ) {
                            Text("Delete Account")
                        }
                    }
                }
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
                                    enabled = account.isActive,
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
                                    enabled = loan.balance > 0.0,
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
                text = "No bank accounts or loans associated with customer"
            )
        }
    }
}

@Composable
private fun CustomerAccountButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    name: String,
    balance: Double,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.height(32.dp)
            .fillMaxWidth()
            .padding(start = 10.dp)
            .clickable { onClick() }
            .then( if (enabled) {
                Modifier
            } else {
                Modifier.background(Color.LightGray)
            })
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = if (enabled) name else "$name {closed}"
        )
        if (enabled) {
            Text(
                modifier = Modifier.align(Alignment.CenterEnd),
                text = balance.formatAsMoney()
            )
        }
    }
}

@Composable
private fun LoanAccountButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    name: String,
    balance: Double,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.height(32.dp)
            .fillMaxWidth()
            .padding(start = 10.dp)
            .clickable { onClick() }
            .then( if (enabled) {
                Modifier
            } else {
                Modifier.background(Color.LightGray)
            })
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = if (enabled) name else "$name {payed off}"
        )
        if (enabled) {
            Text(
                modifier = Modifier.align(Alignment.CenterEnd),
                text = balance.formatAsMoney() + " Owed"
            )
        }
    }
}