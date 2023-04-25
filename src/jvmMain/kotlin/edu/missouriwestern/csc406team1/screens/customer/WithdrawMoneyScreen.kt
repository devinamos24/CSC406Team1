package edu.missouriwestern.csc406team1.screens.customer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.missouriwestern.csc406team1.util.CurrencyAmountInputVisualTransformation
import edu.missouriwestern.csc406team1.util.CustomTextField
import edu.missouriwestern.csc406team1.util.getName
import edu.missouriwestern.csc406team1.viewmodel.customer.WithdrawMoneyScreenViewModel

@Composable
fun CustomerWithdrawMoneyScreen(
    withdrawMoneyScreenViewModel: WithdrawMoneyScreenViewModel,
//    customerRepository: CustomerRepository,
//    accountRepository: AccountRepository,
//    transactionRepository: TransactionRepository,
//    ssn: String,
//    id: String,
//    onBack: () -> Unit
) {

    val customers by withdrawMoneyScreenViewModel.customers.collectAsState()
    val accounts by withdrawMoneyScreenViewModel.accounts.collectAsState()

    val ssn = withdrawMoneyScreenViewModel.ssn
    val id = withdrawMoneyScreenViewModel.id

    val customer = customers.find { it.ssn == ssn }
    val account = accounts.find { it.accountNumber == id }

    val hasFailed by withdrawMoneyScreenViewModel.hasFailed.collectAsState()
    val hasFailedText by withdrawMoneyScreenViewModel.hasFailedText.collectAsState()

    val amount by withdrawMoneyScreenViewModel.amount.collectAsState()

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Button(
                onClick = withdrawMoneyScreenViewModel::onBack
            ) {
                Text("Back")
            }
            if (customer != null && account != null) {
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
                        onValueChange = withdrawMoneyScreenViewModel::onAmountChange,
                    )

                    Button(
                        modifier = Modifier.fillMaxHeight(),
                        onClick = withdrawMoneyScreenViewModel::onWithdraw,
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