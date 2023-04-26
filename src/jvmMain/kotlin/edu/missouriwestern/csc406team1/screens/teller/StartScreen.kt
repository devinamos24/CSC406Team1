package edu.missouriwestern.csc406team1.screens.teller

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TellerStartScreen(
    onBack: () -> Unit,
    onListAccounts: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text("Logout")
        }

        Column (
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.Center)) {
            Button(
                onClick = onListAccounts,
            ) {
                Text("Edit Customer Data")
            }
        }
    }
}

@Composable
@Preview
fun TellerStartScreenPreview () {
    TellerStartScreen(
        onBack = {},
        onListAccounts = {}
    )
}