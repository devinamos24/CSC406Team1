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
import edu.missouriwestern.csc406team1.viewmodel.customer.DepositMoneyScreenViewModel

@Composable
fun CustomerDepositMoneyScreen(
    depositMoneyScreenViewModel: DepositMoneyScreenViewModel,
) {
    val customers by depositMoneyScreenViewModel.customers.collectAsState()
    val accounts by depositMoneyScreenViewModel.accounts.collectAsState()

    val ssn = depositMoneyScreenViewModel.ssn
    val id = depositMoneyScreenViewModel.id

    val customer = customers.find { it.ssn == ssn }
    val account = accounts.find { it.accountNumber == id }

    val amount by depositMoneyScreenViewModel.amount.collectAsState()
    val hasFailed by depositMoneyScreenViewModel.hasFailed.collectAsState()

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Button(
                onClick = depositMoneyScreenViewModel::onBack
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
                        onValueChange = depositMoneyScreenViewModel::onAmountChange
                    )

                    Button(
                        modifier = Modifier.fillMaxHeight(),
                        onClick = depositMoneyScreenViewModel::onDeposit,
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
                        text = "Crediting account failed, try again",
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