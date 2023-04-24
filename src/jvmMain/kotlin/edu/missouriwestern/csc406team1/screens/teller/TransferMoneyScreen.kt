package edu.missouriwestern.csc406team1.screens.teller

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ArrowForward
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
import edu.missouriwestern.csc406team1.util.collectAsState
import edu.missouriwestern.csc406team1.util.formatAsMoney

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TellerTransferMoneyScreen(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    customerSSN: String,
    accountId: String,
    onBack: () -> Unit
) {
    val customers by customerRepository.customers.collectAsState()
    val accounts by accountRepository.accounts.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val customer = customers.find { it.ssn == customerSSN }
    val account = accounts.find { it.accountNumber == accountId }
    val customerAccounts = accounts.filter { it.customerSSN == customerSSN && it.accountNumber != accountId }.sorted()

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

        if (customer != null && account != null) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Selected Account")
                        Text("Balance: ${account.balance.formatAsMoney()}")
                    }
                    Icon(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null
                    )
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
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
                                customerAccounts.forEachIndexed { i, account ->
                                    DropdownMenuItem(
                                        text = { Text(text = "Account $i") },
                                        onClick = {
                                            selectedAccountId = account.accountNumber
                                            selectedAccountText = "Account $i"
                                            expanded = false
                                        },
                                        modifier = Modifier.width(with(LocalDensity.current){textFieldSize.width.toDp()})
                                    )
                                }
                            }
                        }
                        selectedAccount?.let {
                            Text("Balance: ${it.balance.formatAsMoney()}")
                        }
                    }
                }
            }
        } else {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "This account no longer exists"
            )
        }

    }
}