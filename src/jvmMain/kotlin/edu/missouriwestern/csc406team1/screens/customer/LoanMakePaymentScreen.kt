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
import edu.missouriwestern.csc406team1.viewmodel.customer.LoanMakePaymentScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerLoanMakePaymentScreen(
    loanMakePaymentScreenViewModel: LoanMakePaymentScreenViewModel,
) {
    val customers by loanMakePaymentScreenViewModel.customers.collectAsState()
    val accounts by loanMakePaymentScreenViewModel.accounts.collectAsState()
    val loans by loanMakePaymentScreenViewModel.loans.collectAsState()

    val ssn = loanMakePaymentScreenViewModel.ssn
    val id = loanMakePaymentScreenViewModel.id

    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var amountFieldSize by remember { mutableStateOf(Size.Zero) }

    val customer = customers.find { it.ssn == ssn }
    val customerAccounts = accounts.filter { it.customerSSN == ssn && it.isActive && it !is CDAccount }.sorted()
    val loan = loans.find { it.accountNumber == id }

    val selectedAccountId by loanMakePaymentScreenViewModel.selectedAccountId.collectAsState()
    val selectedAccount = accounts.find { it.accountNumber == selectedAccountId }
    val selectedAccountText = selectedAccount?.getName() ?: ""

    val amount by loanMakePaymentScreenViewModel.amount.collectAsState()
    val hasFailed by loanMakePaymentScreenViewModel.hasFailed.collectAsState()

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
                onClick = loanMakePaymentScreenViewModel::onBack
            ) {
                Text("Back")
            }
            if (customer != null && loan != null) {
                Text(
                    text = loan.getName()
                )
            }
        }

        if (customer != null && loan != null && loan.balance > 0) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

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
                                                loanMakePaymentScreenViewModel.onSelectAccount(account.accountNumber)
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
                        } else {
                            Text("No Available Accounts")
                        }
                    }
                    Icon(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null
                    )
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(loan.getName())
                        Text("Balance Owed: ${loan.balance.formatAsMoney()}")
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
                        enabled = selectedAccount != null && selectedAccount.isActive,
                        modifier = Modifier.fillMaxHeight(),
                        onClick = loanMakePaymentScreenViewModel::onClickNone,
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
                        onValueChange = loanMakePaymentScreenViewModel::onAmountChange,
                    )

                    Button(
                        enabled = selectedAccount != null && selectedAccount.isActive,
                        modifier = Modifier.fillMaxHeight(),
                        onClick = loanMakePaymentScreenViewModel::onClickAll,
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
                    onClick = loanMakePaymentScreenViewModel::onPay,
                    enabled = amount.errorMessage == null && amount.value.isNotBlank() && selectedAccount != null && selectedAccount.balance >= amount.value.toDouble() / 100
                ) {
                    Text("Pay")
                }
                if (hasFailed) {
                    Text(
                        text = "Payment failed, try again",
                        color = MaterialTheme.colorScheme.error
                    )
                }

            }

        } else {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "This loan no longer exists or has been payed off"
            )
        }
    }
}