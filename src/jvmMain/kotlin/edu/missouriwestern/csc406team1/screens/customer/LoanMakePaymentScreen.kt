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
import edu.missouriwestern.csc406team1.database.AccountRepository
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.LoanRepository
import edu.missouriwestern.csc406team1.database.TransactionRepository
import edu.missouriwestern.csc406team1.database.model.Transaction
import edu.missouriwestern.csc406team1.database.model.account.CDAccount
import edu.missouriwestern.csc406team1.util.*
import java.lang.NumberFormatException
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerLoanMakePaymentScreen(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    loanRepository: LoanRepository,
    transactionRepository: TransactionRepository,
    ssn: String,
    id: String,
    onBack: () -> Unit,
) {
    val customers by customerRepository.customers.collectAsState()
    val accounts by accountRepository.accounts.collectAsState()
    val loans by loanRepository.loans.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var amountFieldSize by remember { mutableStateOf(Size.Zero) }

    val customer = customers.find { it.ssn == ssn }
    val customerAccounts = accounts.filter { it.customerSSN == ssn && it.isActive && it !is CDAccount }.sorted()
    val loan = loans.find { it.accountNumber == id }

    var selectedAccountId by remember { mutableStateOf("") }
    val selectedAccount = accounts.find { it.accountNumber == selectedAccountId }
    var selectedAccountText by remember { mutableStateOf("") }

    var amount by remember { mutableStateOf(InputWrapper()) }
    var hasFailed by remember { mutableStateOf(false) }

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

        if (customer != null && loan != null &&  loan.balance > 0) {
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
                        enabled = selectedAccount != null && selectedAccount.isActive,
                        modifier = Modifier.fillMaxHeight(),
                        onClick = {
                            if (selectedAccount != null) {
                                amount = if (selectedAccount.balance >= loan.balance) {
                                    amount.copy(value = loan.balance.times(100).toInt().toString())
                                } else {
                                    amount.copy(value = selectedAccount.balance.times(100).toInt().toString())
                                }
                            } else {
                                hasFailed = true
                            }
                        },
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

                            loan.balance -= money
                            selectedAccount!!.balance -= money
                            if (loanRepository.update(loan) && accountRepository.update(selectedAccount)) {
                                //TODO: Implement loans into transactionRepository
//                                transactionRepository.addTransaction(Transaction("", false, true, "p", money, loan.balance, loan.accountNumber, LocalDate.now(), LocalTime.now()))
                                transactionRepository.addTransaction(Transaction("", true, false, "t", money, selectedAccount!!.balance, selectedAccount.accountNumber, LocalDate.now(), LocalTime.now()))
                            } else {
                                hasFailed = true
                            }

                        } catch (e: NumberFormatException) {
                            hasFailed = true
                        }
                    }, // TODO: Transfer Money In ViewModel
                    enabled = amount.errorMessage == null && amount.value.isNotBlank() && selectedAccount != null && selectedAccount.balance >= amount.value.toDouble()/100
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