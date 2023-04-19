package edu.missouriwestern.csc406team1.screens.manager

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.missouriwestern.csc406team1.theme.AppTheme

@Composable
fun ManagerStartScreen(
    onCreateCustomer: () -> Unit,
    onEditCustomerData: () -> Unit,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text("Logout")
        }

        Button(
            onClick = onCreateCustomer,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Text("Create Customer")
        }

        Column(
            modifier = Modifier.align(Alignment.Center).width(IntrinsicSize.Max)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onEditCustomerData,
            ) {
                Text("Edit Customer Data")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {},
            ) {
                Text("Send Bills")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {},
            ) {
                Text("Send Rollover Notices")
            }
        }

    }
}

@Composable
@Preview
fun ManagerStartScreenPreview() {
    AppTheme {
        ManagerStartScreen(
            onCreateCustomer = {},
            onEditCustomerData = {},
            onBack = {}
        )
    }
}