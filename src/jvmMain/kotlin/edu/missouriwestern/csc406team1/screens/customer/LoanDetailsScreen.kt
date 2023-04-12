package edu.missouriwestern.csc406team1.screens.customer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.missouriwestern.csc406team1.util.DateConverter.convertDateToString
import edu.missouriwestern.csc406team1.util.formatAsMoney
import edu.missouriwestern.csc406team1.util.getName
import edu.missouriwestern.csc406team1.viewmodel.customer.LoanDetailsScreenViewModel

@Composable
fun CustomerLoanDetailsScreen(
    loanDetailsScreenViewModel: LoanDetailsScreenViewModel,
) {
    val customers by loanDetailsScreenViewModel.customers.collectAsState()
    val loans by loanDetailsScreenViewModel.loans.collectAsState()

    val ssn = loanDetailsScreenViewModel.ssn
    val id = loanDetailsScreenViewModel.id

    val customer = customers.find { it.ssn == ssn }
    val loan = loans.find { it.accountNumber == id }

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Button(
                onClick = loanDetailsScreenViewModel::onBack
            ) {
                Text("Back")
            }
            if (customer != null && loan != null) {
                Text(
                    text = loan.getName()
                )
            }
        }
        if (customer != null && loan != null && loan.balance > 0) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text("Date Opened: ${convertDateToString(loan.dateOpened)}")
                Text("Current Balance Owed: ${loan.balance.formatAsMoney()}")
                Text("Interest Rate: ${loan.interestRate.times(100)}%")
                Text("Next Payment Due: ${convertDateToString(loan.datePaymentDue)} Amount: ${loan.currentPaymentDue.formatAsMoney()}")
                Text("Last Payment Date: ${convertDateToString(loan.dateSinceLastPayment)}")
                if (loan.missedPayment) {
                    Text(
                        color = MaterialTheme.colorScheme.error,
                        text = "Payment Missed"
                    )
                }
                Row (
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = loanDetailsScreenViewModel::onMakePayment
                    ) {
                        Text("Make Payment")
                    }
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