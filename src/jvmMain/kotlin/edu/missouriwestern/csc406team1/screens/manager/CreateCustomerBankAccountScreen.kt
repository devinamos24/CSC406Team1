package edu.missouriwestern.csc406team1.screens.manager

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
import edu.missouriwestern.csc406team1.database.AccountRepository
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.model.account.*
import edu.missouriwestern.csc406team1.util.*
import edu.missouriwestern.csc406team1.util.DateConverter.convertStringToDate
import edu.missouriwestern.csc406team1.util.InputValidator.getAccountTypeErrorOrNull
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerCreateCustomerBankAccount(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    ssn: String,
    onCreate: (String, String) -> Unit,
    onBack: () -> Unit
) {

    var type by remember { mutableStateOf(InputWrapper()) }
    var initialBalance by remember { mutableStateOf(InputWrapper()) }
    var interestRate by remember { mutableStateOf(InputWrapper()) }
    var cdDueDate by remember { mutableStateOf(InputWrapper()) }

    var expandedType by remember { mutableStateOf(false) }
    var expandedBackup by remember { mutableStateOf(false) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

//    fun inputsValid(): Boolean {
//        return type.errorMessage == null && type.value.isNotBlank()
//                && initialBalance.errorMessage == null && initialBalance.value.isNotBlank()
//                && interestRate.errorMessage == null && interestRate.value.isNotBlank()
//                && cdDueDate.errorMessage == null && cdDueDate.value.isNotBlank()
//    }

    val customers by customerRepository.customers.collectAsState()
    val accounts by accountRepository.accounts.collectAsState()

    val customer = customers.find { it.ssn == ssn }
    val customerAccounts = accounts.filter { it.customerSSN == ssn && it is SavingsAccount && it !is CDAccount && it.isActive }

    var selectedAccountId by remember { mutableStateOf("") }
    val selectedAccount = accounts.find { it.accountNumber == selectedAccountId && it is SavingsAccount }
    var selectedAccountText by remember { mutableStateOf("") }

    val iconType = if (expandedType)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

    val iconBackup = if (expandedBackup)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(
            modifier = Modifier.align(Alignment.TopStart),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Button(
                onClick = onBack
            ) {
                Text("Back")
            }
            if (customer != null) {
                Text(
                    text = "${customer.firstname} ${customer.lastname}"
                )
            }
        }

        if (customer != null) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box {
                    TextField(
                        isError = type.errorMessage != null,
                        value =
                            when (type.value) {
                                "CD" -> "CD"
                                "S" -> "Savings"
                                "TMB" -> "That's My Bank"
                                "GD" -> "Gold/Diamond"
                                else -> ""
                            },
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .onGloballyPositioned { coordinates ->
                                //This value is used to assign to the DropDown the same width
                                textFieldSize = coordinates.size.toSize()
                            }.clickable { expandedType = !expandedType },
                        label = { Text("Account Type") },
                        trailingIcon = { Icon(iconType, null, Modifier.clickable { expandedType = !expandedType }) }
                    )
                    DropdownMenu(
                        expanded = expandedType,
                        onDismissRequest = { expandedType = false }
                    ) {
                        listOf("CD", "S", "TMB", "GD").forEach { accountType ->
                            DropdownMenuItem(
                                text = { Text(
                                    when (accountType) {
                                        "CD" -> "CD"
                                        "S" -> "Savings"
                                        "TMB" -> "That's My Bank"
                                        "GD" -> "Gold/Diamond"
                                        else -> ""
                                    })
                                },
                                onClick = {
                                    type = type.copy(value = accountType, errorMessage = getAccountTypeErrorOrNull(accountType))
                                    expandedType = false
                                },
                                modifier = Modifier.width(with(LocalDensity.current){textFieldSize.width.toDp()})
                            )
                        }
                    }
                }

                if (type.value.isNotBlank()) {
                    CustomTextField(
                        label = "Initial Balance",
                        inputWrapper = initialBalance,
                        shape = RoundedCornerShape(topStart = 4.dp),
                        visualTransformation = CurrencyAmountInputVisualTransformation(),
                        onValueChange = {
                            if (it.all { character -> character.isDigit() }) {
                                initialBalance = initialBalance.copy(value = it)
                            }
                        }
                    )
                }

                if (type.value == "CD" || type.value == "S" || type.value == "GD") {
                    CustomTextField(
                        label = "Interest Rate",
                        inputWrapper = interestRate,
                        onValueChange = {
                            if (it.all { character -> character.isDigit() || character == '.' }) {
                                interestRate = interestRate.copy(value = it) // TODO: Implement error stuff
                            }
                        }
                    )
                }

                if (type.value == "CD") {
                    CustomTextField(
                        label = "CD Due Date",
                        inputWrapper = cdDueDate,
                        onValueChange = {
                            if (true) {
                                cdDueDate = cdDueDate.copy(value = it) // TODO: Create error stuff and input mapper
                            }
                        }
                    )
                }

                if (type.value == "TMB" || type.value == "GD") {
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
                                        }.clickable { expandedBackup = !expandedBackup },
                                    label = { Text("Backup Account") },
                                    trailingIcon = { Icon(iconBackup, null, Modifier.clickable { expandedBackup = !expandedBackup }) }
                                )
                                DropdownMenu(
                                    expanded = expandedBackup,
                                    onDismissRequest = { expandedBackup = false }
                                ) {
                                    customerAccounts.forEach { account ->
                                        DropdownMenuItem(
                                            text = { Text(text = account.getName()) },
                                            onClick = {
                                                selectedAccountId = account.accountNumber
                                                selectedAccountText = account.getName()
                                                expandedBackup = false
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

                Button(
                    onClick = {

                        try {
                            val account: Account? = when(type.value) {
                                "CD" -> CDAccount("", ssn, (initialBalance.value.toDouble()/100), LocalDate.now(), true, interestRate.value.toDouble(), convertStringToDate(cdDueDate.value))
                                "S" -> SavingsAccount("", ssn, (initialBalance.value.toDouble()/100), LocalDate.now(), true, interestRate.value.toDouble())
                                "TMB" -> TMBAccount("", ssn, (initialBalance.value.toDouble()/100), LocalDate.now(), true, false, if (selectedAccount == null) null else selectedAccount as SavingsAccount, 0)
                                "GD" -> GoldDiamondAccount("", ssn, (initialBalance.value.toDouble()/100), LocalDate.now(), true, interestRate.value.toDouble(), if (selectedAccount == null) null else selectedAccount as SavingsAccount, false, 0)
                                else -> null
                            }

                            if (account != null && accountRepository.addAccount(account)) {
                                onCreate(ssn, account.accountNumber)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
//                    enabled = inputsValid()
                ) {
                    Text("Create")
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