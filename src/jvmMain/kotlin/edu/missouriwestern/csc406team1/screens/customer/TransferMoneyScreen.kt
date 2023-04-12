package edu.missouriwestern.csc406team1.screens.customer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import edu.missouriwestern.csc406team1.database.model.account.CDAccount
import edu.missouriwestern.csc406team1.util.*
import edu.missouriwestern.csc406team1.viewmodel.customer.TransferMoneyScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerTransferMoneyScreen(
    transferMoneyScreenViewModel: TransferMoneyScreenViewModel,
) {
    val customers by transferMoneyScreenViewModel.customers.collectAsState()
    val accounts by transferMoneyScreenViewModel.accounts.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var amountFieldSize by remember { mutableStateOf(Size.Zero) }

    val ssn = transferMoneyScreenViewModel.ssn
    val id = transferMoneyScreenViewModel.id

    val customer = customers.find { it.ssn == ssn }
    val account = accounts.find { it.accountNumber == id }
    val customerAccounts =
        accounts.filter { it.customerSSN == ssn && it.accountNumber != id && it.isActive && it !is CDAccount }.sorted()

    val selectedAccountId by transferMoneyScreenViewModel.selectedAccountId.collectAsState()
    val selectedAccount = accounts.find { it.accountNumber == selectedAccountId }
    val selectedAccountText = selectedAccount?.getName() ?: ""

    val amount by transferMoneyScreenViewModel.amount.collectAsState()
    val hasFailed by transferMoneyScreenViewModel.hasFailed.collectAsState()

    val icon = if (expanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Button(
                onClick = transferMoneyScreenViewModel::onBack
            ) {
                Text("Back")
            }
            if (customer != null && account != null) {
                Text(
                    text = account.getName()
                )
            }
        }

        if (customer != null && account != null && account.isActive) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(account.getName())
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
                        if (customerAccounts.isNotEmpty()) {
                            Box {
                                TextField(
                                    value = selectedAccountText,
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
                                                transferMoneyScreenViewModel.onSelectAccount(account.accountNumber)
                                                expanded = false
                                            },
                                            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                                        )
                                    }
                                }
                            }
                            if (selectedAccount != null) {
                                Text("Balance: ${selectedAccount.balance.formatAsMoney()}")
                            }
                        } else {
                            Text("No Available Accounts")
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(IntrinsicSize.Max)

                ) {
                    Button(
                        modifier = Modifier.fillMaxHeight(),
                        onClick = transferMoneyScreenViewModel::onClickNone,
                        shape = MaterialTheme.shapes.extraLarge.copy(
                            topEnd = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp)
                        )
                    ) {
                        Text("None")
                    }

                    CustomTextField(
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            //This value is used to assign to the DropDown the same width
                            amountFieldSize = coordinates.size.toSize()
                        },
                        label = "Amount",
                        inputWrapper = amount,
                        shape = RoundedCornerShape(topStart = 4.dp),
                        visualTransformation = CurrencyAmountInputVisualTransformation(),
                        onValueChange = transferMoneyScreenViewModel::onAmountChange
                    )

                    Button(
                        modifier = Modifier.fillMaxHeight(),
                        onClick = transferMoneyScreenViewModel::onClickAll,
                        shape = MaterialTheme.shapes.extraLarge.copy(
                            topStart = CornerSize(0.dp),
                            bottomStart = CornerSize(0.dp)
                        )
                    ) {
                        Text("All")
                    }

                }

                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(with(LocalDensity.current) { amountFieldSize.width.toDp() }),
                    onClick = transferMoneyScreenViewModel::onTransfer,
                    enabled = amount.errorMessage == null && amount.value.isNotBlank() && account.balance >= amount.value.toDouble() / 100 && selectedAccount != null
                ) {
                    Text("Transfer")
                }
                if (hasFailed) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = "Transfer failed, try again",
                        color = MaterialTheme.colorScheme.error
                    )
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