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
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.LoanRepository
import edu.missouriwestern.csc406team1.database.TransactionRepository
import edu.missouriwestern.csc406team1.util.*

//TODO: Finish this screen

@Composable
fun ManagerCreditCustomerLoanScreen(
    customerRepository: CustomerRepository,
    loanRepository: LoanRepository,
    transactionRepository: TransactionRepository,
    ssn: String,
    id: String,
    onBack: () -> Unit
) {

    val customers by customerRepository.customers.collectAsState()
    val loans by loanRepository.loans.collectAsState()

    val customer = customers.find { it.ssn == ssn }
    val loan = loans.find { it.accountNumber == id }

    var hasFailed by remember { mutableStateOf(false) }
    var amount by remember { mutableStateOf(InputWrapper()) }

    val enabled = try {
        val money = amount.value.toDouble() / 100
        money <= loan!!.balance
    } catch (e: Exception) {
        false
    }

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
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
                    text = loan.getName()
                )
            }
        }

        if (customer != null && loan != null && loan.balance > 0.0) {

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Current Balance Owed: ${loan.balance.formatAsMoney()}")
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

                                loan.balance -= money
                                if (loanRepository.update(loan.copy())) {
                                    onBack()
                                } else {
                                    hasFailed = true
                                }

                            } catch (e: NumberFormatException) {
                                hasFailed = true
                            }
                        },
                        enabled = enabled && amount.value.isNotBlank(),
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
                        text = "Crediting loan failed, try again",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        } else {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "This loan no longer exists or is payed off"
            )
        }
    }
}