package edu.missouriwestern.csc406team1.screens.manager

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
import androidx.compose.ui.window.WindowScope
import edu.missouriwestern.csc406team1.database.AccountRepository
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.TransactionRepository
import edu.missouriwestern.csc406team1.database.model.Transaction
import edu.missouriwestern.csc406team1.database.model.account.CDAccount
import edu.missouriwestern.csc406team1.util.*
import java.lang.NumberFormatException
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WindowScope.ManagerCloseCustomerBankAccountScreen(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    transactionRepository: TransactionRepository,
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

    var amount by remember { mutableStateOf(InputWrapper()) }
    var hasFailed by remember { mutableStateOf(false) }

    val icon = if (expanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

    var printingCheck by remember { mutableStateOf(false) }
    var closingAccount by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text("Back")
        }

        if (customer != null && account != null &&  account.isActive) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {

                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Account balance must be $0 to close"
                )

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
                            selectedAccount?.let {
                                Text("Balance: ${it.balance.formatAsMoney()}")
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
                        onClick = { amount = amount.copy(value = "") },
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
                        onValueChange = {
                            if (it.all { character -> character.isDigit() }) {
                                amount = amount.copy(value = it)
                            }
                        }
                    )

                    Button(
                        modifier = Modifier.fillMaxHeight(),
                        onClick = { amount = amount.copy(value = account.balance.times(100).toInt().toString()) },
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
                        .width(with(LocalDensity.current){amountFieldSize.width.toDp()}),
                    onClick = {
                        try {
                            val money = amount.value.toDouble() / 100

                            account.balance -= money
                            selectedAccount!!.balance += money
                            if (accountRepository.update(account) && accountRepository.update(selectedAccount)) {
                                transactionRepository.addTransaction(Transaction("", false, true, "t", money, account.balance, account.accountNumber, LocalDate.now(), LocalTime.now()))
                                transactionRepository.addTransaction(Transaction("", true, false, "t", money, selectedAccount!!.balance, selectedAccount.accountNumber, LocalDate.now(), LocalTime.now()))
                            } else {
                                hasFailed = true
                            }

                        } catch (e: NumberFormatException) {
                            hasFailed = true
                        }
                    }, // TODO: Transfer Money In ViewModel
                    enabled = amount.errorMessage == null && amount.value.isNotBlank() && account.balance >= amount.value.toDouble()/100 && selectedAccount != null
                ) {
                    Text("Transfer")
                }

                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(with(LocalDensity.current){amountFieldSize.width.toDp()}),
                    onClick = {
                        printingCheck = true
                    },
                    enabled = account.balance > 0.0
                ) {
                    Text("Print Check")
                }
                if (hasFailed) {
                    Text(
                        text = "Transfer failed, try again",
                        color = MaterialTheme.colorScheme.error
                    )
                }

            }

            if (printingCheck) {
                YesNoCancelDialog("Print", "Are you sure you want to print check for ${account.balance.formatAsMoney()} and close the account? This action cannot be undone.") {
                    when (it) {
                        AlertDialogResult.Yes -> {
                            transactionRepository.addTransaction(Transaction("", false, true, "ch", account.balance, 0.0, account.accountNumber, LocalDate.now(), LocalTime.now()))
                            accountRepository.update(account.apply { this.balance = 0.0; this.isActive = false })
                            // TODO: Put check in database
                            onBack()
                        }
                        else -> {
                            printingCheck = false
                        }
                    }
                }
            }

            if (closingAccount) {
                YesNoCancelDialog("Close Account", "Are you sure you want to close this account? This action cannot be undone.") {
                    when (it) {
                        AlertDialogResult.Yes -> {
                            accountRepository.update(account.apply { this.balance = 0.0; this.isActive = false })
                            onBack()
                        }
                        else -> {
                            printingCheck = false
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
        if (customer != null && account != null && account.isActive) {
            Button(
                modifier = Modifier.align(Alignment.BottomEnd),
                enabled = account.balance == 0.0,
                onClick = {
                    closingAccount = true
                }
            ) {
                Text("Confirm")
            }
        }
    }
}