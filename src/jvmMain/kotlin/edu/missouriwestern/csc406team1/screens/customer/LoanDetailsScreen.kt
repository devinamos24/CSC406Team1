package edu.missouriwestern.csc406team1.screens.customer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.LoanRepository
import edu.missouriwestern.csc406team1.database.model.loan.CreditCardLoan
import edu.missouriwestern.csc406team1.database.model.loan.MortgageLoan
import edu.missouriwestern.csc406team1.database.model.loan.ShortTermLoan
import edu.missouriwestern.csc406team1.util.DateConverter.convertDateToString
import edu.missouriwestern.csc406team1.util.collectAsState
import edu.missouriwestern.csc406team1.util.formatAsMoney

@Composable
fun CustomerLoanDetailsScreen(
    customerRepository: CustomerRepository,
    loanRepository: LoanRepository,
    ssn: String,
    id: String,
    onBack: () -> Unit
) {
    val customers by customerRepository.customers.collectAsState()
    val loans by loanRepository.loans.collectAsState()

    val customer = customers.find { it.ssn == ssn }
    val loan = loans.find { it.accountNumber == id }

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text("Back")
        }
        if (customer != null && loan != null && loan.balance > 0) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                val detailsModifier = Modifier.align(Alignment.CenterHorizontally)
                when (loan) {
                    is MortgageLoan -> MortgageLoanDetails(detailsModifier, loan)
                    is ShortTermLoan -> ShortTermLoanDetails(detailsModifier, loan)
                    is CreditCardLoan -> CreditCardLoanDetails(detailsModifier, loan)
                }
                Row (
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = {  }
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

@Composable
private fun MortgageLoanDetails(
    modifier: Modifier = Modifier,
    loan: MortgageLoan
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${convertDateToString(loan.dateOpened)}")
        Text("Current Balance Owed: ${loan.balance.formatAsMoney()}")
        Text("Interest Rate: ${loan.interestRate.times(100)}%}")
        Text("Next Payment Due: ${convertDateToString(loan.datePaymentDue)} Amount: ${loan.currentPaymentDue.formatAsMoney()}")
        Text("Last Payment Date: ${convertDateToString(loan.dateSinceLastPayment)}")
        if (loan.missedPayment) {
            Text(
                color = MaterialTheme.colorScheme.error,
                text = "Payment Missed"
            )
        }
    }
}

@Composable
private fun ShortTermLoanDetails(
    modifier: Modifier = Modifier,
    loan: ShortTermLoan
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${convertDateToString(loan.dateOpened)}")
        Text("Current Balance Owed: ${loan.balance.formatAsMoney()}")
        Text("Interest Rate: ${loan.interestRate.times(100)}%}")
        Text("Next Payment Due: ${convertDateToString(loan.datePaymentDue)} Amount: ${loan.currentPaymentDue.formatAsMoney()}")
        Text("Last Payment Date: ${convertDateToString(loan.dateSinceLastPayment)}")
        if (loan.missedPayment) {
            Text(
                color = MaterialTheme.colorScheme.error,
                text = "Payment Missed"
            )
        }
    }
}

@Composable
private fun CreditCardLoanDetails(
    modifier: Modifier = Modifier,
    loan: CreditCardLoan
) {
    Column(
        modifier = modifier
    ) {
        Text("Date Opened: ${convertDateToString(loan.dateOpened)}")
        Text("Current Balance Owed: ${loan.balance.formatAsMoney()}")
        Text("Interest Rate: ${loan.interestRate.times(100)}%}")
        Text("Next Payment Due: ${convertDateToString(loan.datePaymentDue)} Amount: ${loan.currentPaymentDue.formatAsMoney()}")
        Text("Last Payment Date: ${convertDateToString(loan.dateSinceLastPayment)}")
        if (loan.missedPayment) {
            Text(
                color = MaterialTheme.colorScheme.error,
                text = "Payment Missed"
            )
        }
    }
}