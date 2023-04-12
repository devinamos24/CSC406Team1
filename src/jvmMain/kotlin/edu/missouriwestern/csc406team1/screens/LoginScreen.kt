package edu.missouriwestern.csc406team1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * This screen is where the user will select who they are
 */
@Composable
fun LoginScreen(
    onClickCustomer: () -> Unit,
    onClickBankTeller: () -> Unit,
    onClickBankManager: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Who are you?",
            )
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(onClick = onClickCustomer) {
                    Text("Customer")
                }
                Button(onClick = onClickBankTeller) {
                    Text("Bank Teller")
                }
                Button(onClick = onClickBankManager) {
                    Text("Bank Manager")
                }
            }
        }
    }
}