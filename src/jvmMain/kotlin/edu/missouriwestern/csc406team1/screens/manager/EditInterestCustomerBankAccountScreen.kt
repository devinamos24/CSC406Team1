package edu.missouriwestern.csc406team1.screens.manager

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// TODO: Finish this screen

@Composable
fun ManagerEditInterestCustomerBankAccountScreen(
    onBack: () -> Unit
) {
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
//            if (customer != null && account != null) {
//                Text(
//                    text = "${customer.firstname} ${customer.lastname}"
//                )
//                Text(
//                    text = account.getName()
//                )
//            }
        }
    }
}