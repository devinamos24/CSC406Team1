package edu.missouriwestern.csc406team1.screens.manager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.missouriwestern.csc406team1.database.AccountRepository
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.TransactionRepository
import edu.missouriwestern.csc406team1.database.model.Transaction
import edu.missouriwestern.csc406team1.util.*
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun ManagerDebitCustomerBankAccountScreen(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    transactionRepository: TransactionRepository,
    ssn: String,
    id: String,
    onBack: () -> Unit
) {

    val customers by customerRepository.customers.collectAsState()
    val accounts by accountRepository.accounts.collectAsState()

    val customer = customers.find { it.ssn == ssn }
    val account = accounts.find { it.accountNumber == id }

    var hasFailed by remember { mutableStateOf(false) }
    var hasFailedText by remember { mutableStateOf("Debiting account failed, try again") }

    var amount by remember { mutableStateOf(InputWrapper()) }

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(
            modifier = Modifier.align(Alignment.TopStart),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Button(
                onClick = onBack
            ) {
                Text("Back")
            }
            if (customer != null && account != null) {
                Text(
                    text = "${customer.firstname} ${customer.lastname}"
                )
                Text(
                    text = account.getName()
                )
            }
        }

        if (customer != null && account != null && account.isActive) {

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomTextField(
                        label = "Amount",
                        inputWrapper = amount,
                        shape = RoundedCornerShape(topStart = 4.dp),
                        visualTransformation = CurrencyAmountInputVisualTransformation(),
                        onValueChange = {
                            if (it.all { character -> character.isDigit() }) {
                                amount = amount.copy(value = it)
                            }
                        }
                    )

                    Button(
                        modifier = Modifier.fillMaxHeight(),
                        onClick = {
                            try {
                                val money = amount.value.toDouble() / 100

                                if (account.balance >= money) {
                                    account.balance -= money
                                    if (accountRepository.update(account)) {
                                        transactionRepository.addTransaction(Transaction("", false, true, "d", money, account.balance, account.accountNumber, LocalDate.now(), LocalTime.now()))
                                        onBack()
                                    } else {
                                        hasFailed = true
                                    }
                                } else {
                                    hasFailedText = "Insufficient funds in account"
                                    hasFailed = true
                                }

                            } catch (e: NumberFormatException) {
                                hasFailed = true
                            }
                        },
                        enabled = amount.errorMessage == null && amount.value.isNotBlank(),
                        shape = MaterialTheme.shapes.extraLarge.copy(
                            topStart = CornerSize(0.dp),
                            bottomStart = CornerSize(0.dp)
                        )
                    ) {
                        Text("Confirm")
                    }
                }
                if (hasFailed) {
                    Text(
                        text = hasFailedText,
                        color = MaterialTheme.colorScheme.error
                    )
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