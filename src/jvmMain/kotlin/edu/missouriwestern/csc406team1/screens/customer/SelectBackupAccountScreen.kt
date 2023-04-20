package edu.missouriwestern.csc406team1.screens.customer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import edu.missouriwestern.csc406team1.database.AccountRepository
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.model.account.CDAccount
import edu.missouriwestern.csc406team1.util.collectAsState
import edu.missouriwestern.csc406team1.util.getName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerSelectBackupAccountScreen(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    ssn: String,
    id: String,
    onBack: () -> Unit,
) {
    val customers by customerRepository.customers.collectAsState()
    val accounts by accountRepository.accounts.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var amountFieldSize by remember { mutableStateOf(Size.Zero) }

    val customer = customers.find { it.ssn == ssn }
    val account = accounts.find { it.accountNumber == id }
    val customerAccounts = accounts.filter { it.customerSSN == ssn && it.accountNumber != id && it.isActive && it !is CDAccount }.sorted()

    var selectedAccountId by remember { mutableStateOf("") }
    val selectedAccount = accounts.find { it.accountNumber == selectedAccountId }
    var selectedAccountText by remember { mutableStateOf("") }

    val icon = if (expanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown
    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text("Back")
        }

        if (customer != null && account != null && account.isActive) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                if (customerAccounts.isNotEmpty()) {
                    Box {
                        TextField(
                            value = if (selectedAccount != null) selectedAccountText else "",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .onGloballyPositioned { coordinates ->
                                    //This value is used to assign to the DropDown the same width
                                    textFieldSize = coordinates.size.toSize()
                                }.clickable { expanded = !expanded },
                            label = { Text("Account") },
                            trailingIcon = { Icon(icon, null, Modifier.clickable { expanded = !expanded }) }
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            customerAccounts.forEach { account ->
                                DropdownMenuItem(
                                    text = { Text(text = account.getName()) },
                                    onClick = {
                                        selectedAccountId = account.accountNumber
                                        selectedAccountText = account.getName()
                                        expanded = false
                                    },
                                    modifier = Modifier.width(with(LocalDensity.current){textFieldSize.width.toDp()})
                                )
                            }
                        }
                    }
                } else {
                    Text("No Available Accounts")
                }
            }
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "This account no longer exists"
        )
    }
}