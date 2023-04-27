package edu.missouriwestern.csc406team1.screens.manager

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import edu.missouriwestern.csc406team1.util.*
import edu.missouriwestern.csc406team1.viewmodel.manager.EditCustomerLoanScreenViewModel

@Composable
fun WindowScope.ManagerEditCustomerLoanScreen(
    editCustomerLoanScreenViewModel: EditCustomerLoanScreenViewModel,
) {
    val customers by editCustomerLoanScreenViewModel.customers.collectAsState()
    val loans by editCustomerLoanScreenViewModel.loans.collectAsState()

    val ssn = editCustomerLoanScreenViewModel.ssn
    val id = editCustomerLoanScreenViewModel.id

    val customer = customers.find { it.ssn == ssn }
    val loan = loans.find { it.accountNumber == id }

    var releasingLien by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Button(
                onClick = editCustomerLoanScreenViewModel::onBack
            ) {
                Text("Back")
            }
            if (customer != null && loan != null) {
                Text(
                    text = loan.getName()
                )
            }
        }
        if (customer != null && loan != null) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text("Date Opened: ${DateConverter.convertDateToString(loan.dateOpened)}")
                if (loan.balance > 0.0) {
                    Text("Current Balance Owed: ${loan.balance.formatAsMoney()}")
                    Text("Interest Rate: ${loan.interestRate.times(100)}%")
                    Text("Next Payment Due: ${DateConverter.convertDateToString(loan.datePaymentDue)} Amount: ${loan.currentPaymentDue.formatAsMoney()}")
                    Text("Last Payment Date: ${DateConverter.convertDateToString(loan.dateSinceLastPayment)}")
                    if (loan.missedPayment) {
                        Text(
                            color = MaterialTheme.colorScheme.error,
                            text = "Payment Missed"
                        )
                    }
                } else {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = "Payed Off",
                        color = Color.Green
                    )
                }

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (loan.balance > 0.0) {
                        Button(
                            onClick = editCustomerLoanScreenViewModel::onCredit
                        ) {
                            Text("Credit")
                        }
                        Button(
                            onClick = { releasingLien = true }
                        ) {
                            Text("Release Lien")
                        }
                    }
                    Button(
                        onClick = editCustomerLoanScreenViewModel::onViewTransactionHistory
                    ) {
                        Text("View Payment History")
                    }
                }
            }

            if (releasingLien) {
                YesNoCancelDialog("Release Lien", "Are you sure you want to release this lien? This action cannot be undone.") {
                    when (it) {
                        AlertDialogResult.Yes -> editCustomerLoanScreenViewModel.onReleaseLien()
                        else -> {
                            releasingLien = false
                        }
                    }
                }
            }

        } else {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "This loan no longer exists"
            )
        }
    }
}