package edu.missouriwestern.csc406team1.screens.manager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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

@Composable
fun ManagerCreateCustomerBankAccount(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    ssn: String,
    onCreate: () -> Unit,
    onBack: () -> Unit
) {

    val customers by customerRepository.customers.collectAsState()
    val accounts by accountRepository.accounts.collectAsState()

    val customer = customers.find { it.ssn == ssn }

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text("Back")
        }

        if (customer != null) {

        } else {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "This account no longer exists"
            )
        }
    }
}