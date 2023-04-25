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
import edu.missouriwestern.csc406team1.util.CustomTextField
import edu.missouriwestern.csc406team1.viewmodel.customer.LoginScreenViewModel

/**
 * This screen is where the customer will log into their account
 */
@Composable
fun CustomerLoginScreen(
    loginScreenViewModel: LoginScreenViewModel,
) {
    val hasFailed by loginScreenViewModel.hasFailed.collectAsState()
    val ssn by loginScreenViewModel.ssn.collectAsState()

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Button(
            onClick = loginScreenViewModel::onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text("Back")
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    label = "SSN",
                    inputWrapper = ssn,
                    shape = RoundedCornerShape(topStart = 4.dp),
                    onValueChange = { loginScreenViewModel.onSSNChange(it) }
                )

                Button(
                    modifier = Modifier.fillMaxHeight(),
                    onClick = loginScreenViewModel::onClickLogin ,
                    enabled = ssn.errorMessage == null && ssn.value.isNotBlank(),
                    shape = MaterialTheme.shapes.extraLarge.copy(
                        topStart = CornerSize(0.dp),
                        bottomStart = CornerSize(0.dp)
                    )
                ) {
                    Text("Login")
                }
            }
            if (hasFailed) {
                Text(
                    text = "That ssn does not exist in database, try making a new account",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}