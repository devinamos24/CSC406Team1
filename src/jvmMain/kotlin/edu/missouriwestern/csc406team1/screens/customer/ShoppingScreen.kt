package edu.missouriwestern.csc406team1.screens.customer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import edu.missouriwestern.csc406team1.database.model.account.Account
import edu.missouriwestern.csc406team1.database.model.account.CheckingAccount
import edu.missouriwestern.csc406team1.database.model.loan.CreditCardLoan
import edu.missouriwestern.csc406team1.database.model.loan.Loan
import edu.missouriwestern.csc406team1.util.CurrencyAmountInputVisualTransformation
import edu.missouriwestern.csc406team1.util.CustomTextField
import edu.missouriwestern.csc406team1.util.formatAsMoney
import edu.missouriwestern.csc406team1.util.getName
import edu.missouriwestern.csc406team1.viewmodel.customer.ShoppingScreenViewModel

// TODO: Finish this screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerShoppingScreen(
    shoppingScreenViewModel: ShoppingScreenViewModel,
) {

    val customers by shoppingScreenViewModel.customers.collectAsState()
    val accounts by shoppingScreenViewModel.accounts.collectAsState()
    val loans by shoppingScreenViewModel.loans.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var amountFieldSize by remember { mutableStateOf(Size.Zero) }

    val ssn = shoppingScreenViewModel.ssn

    val customer = customers.find { it.ssn == ssn }
    val customerAccounts = accounts.filter { it.customerSSN == ssn && it is CheckingAccount && it.isActive }
    val customerCreditCards = loans.filter { it.customerSSN == ssn && it is CreditCardLoan }

    val hasFailed by shoppingScreenViewModel.hasFailed.collectAsState()
    val hasFailedText by shoppingScreenViewModel.hasFailedText.collectAsState()

    val selectedAccountId by shoppingScreenViewModel.selectedAccountId.collectAsState()
    val isAccount by shoppingScreenViewModel.isAccount.collectAsState()
    val selectedAccount = if (isAccount) accounts.find { it.accountNumber == selectedAccountId } else loans.find { it.accountNumber == selectedAccountId }
    val selectedAccountText = if (selectedAccount is Loan) selectedAccount.getName() else if (selectedAccount is Account) selectedAccount.getName() else ""

    val amount by shoppingScreenViewModel.amount.collectAsState()

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
                onClick = shoppingScreenViewModel::onBack
            ) {
                Text("Back")
            }
        }

        if (customer != null) {

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                                            shoppingScreenViewModel.setIsAccount(true)
                                            shoppingScreenViewModel.onSelectAccount(account.accountNumber)
                                            expanded = false
                                        },
                                        modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                                    )
                                }
                                customerCreditCards.forEach { creditCard ->
                                    DropdownMenuItem(
                                        text = { Text(text = creditCard.getName()) },
                                        onClick = {
                                            shoppingScreenViewModel.setIsAccount(false)
                                            shoppingScreenViewModel.onSelectAccount(creditCard.accountNumber)
                                            expanded = false
                                        },
                                        modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                                    )
                                }
                            }
                        }
                        if (selectedAccount != null) {
                            if (selectedAccount is Account) {
                                Text("Balance: ${selectedAccount.balance.formatAsMoney()}")
                            } else if (selectedAccount is CreditCardLoan) {
                                Text("Balance: ${(selectedAccount.creditLimit - selectedAccount.balance).formatAsMoney()}")
                            }
                        }
                    } else {
                        Text("No Available Accounts")
                    }
                }
                CustomTextField(
                    label = "Amount",
                    inputWrapper = amount,
                    shape = RoundedCornerShape(topStart = 4.dp),
                    visualTransformation = CurrencyAmountInputVisualTransformation(),
                    onValueChange = shoppingScreenViewModel::onAmountChange,
                )
                Row {
                    if (selectedAccount is Account) {
                        Button(
                            onClick = shoppingScreenViewModel::onMakePurchaseATM,
                            enabled = selectedAccount.isActive && selectedAccount is CheckingAccount && selectedAccount.atmCard != null && amount.errorMessage == null && amount.value.isNotBlank(),
                        ) {
                            Text("Purchase with ATM card")
                        }
                        Button(
                            onClick = shoppingScreenViewModel::onMakePurchaseCheck,
                            enabled = selectedAccount.isActive && selectedAccount is CheckingAccount && amount.errorMessage == null && amount.value.isNotBlank(),
                        ) {
                            Text("Purchase with check")
                        }
                    } else if (selectedAccount is CreditCardLoan) {
                        Button(
                            onClick = shoppingScreenViewModel::onMakePurchaseCreditCard,
                            enabled = amount.errorMessage == null && amount.value.isNotBlank()
                        ) {
                            Text("Purchase with Credit Card")
                        }
                    }
                }
                if (hasFailed) {
                    Text(
                        text = hasFailedText,
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