package edu.missouriwestern.csc406team1.screens.manager

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
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.LoanRepository
import edu.missouriwestern.csc406team1.database.model.account.*
import edu.missouriwestern.csc406team1.database.model.loan.CreditCardLoan
import edu.missouriwestern.csc406team1.database.model.loan.Loan
import edu.missouriwestern.csc406team1.database.model.loan.MortgageLoan
import edu.missouriwestern.csc406team1.database.model.loan.ShortTermLoan
import edu.missouriwestern.csc406team1.util.*
import edu.missouriwestern.csc406team1.util.DateConverter.*
import edu.missouriwestern.csc406team1.util.InputValidator.getBalanceErrorOrNull
import edu.missouriwestern.csc406team1.util.InputValidator.getInterestRateErrorOrNull
import edu.missouriwestern.csc406team1.util.InputValidator.getLengthErrorOrNull
import java.time.LocalDate
import kotlin.math.pow

//TODO: Finish this screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerCreateCustomerLoanScreen(
    customerRepository: CustomerRepository,
    loanRepository: LoanRepository,
    ssn: String,
    onCreate: (String, String) -> Unit,
    onBack: () -> Unit
) {

    var type by remember { mutableStateOf(InputWrapper()) }
    var balance by remember { mutableStateOf(InputWrapper()) }
    var length by remember { mutableStateOf(InputWrapper()) }
    var interestRate by remember { mutableStateOf(InputWrapper()) }
    val paymentDueDate = LocalDate.now().plusMonths(1)
    val paymentPer = try {
        ((balance.value.toDouble()/100) * (interestRate.value.toDouble()/10000/12)) / (1 - (1 + interestRate.value.toDouble()/10000/12).pow(-12*length.value.toInt()))
    } catch (ignored: Exception) {
        0.0
    }
    var creditLimit by remember { mutableStateOf(InputWrapper()) }

    fun inputsValid(): Boolean {
        return type.errorMessage == null && type.value.isNotBlank()
                && !(type.value != "cc" && (balance.errorMessage != null && balance.value.isBlank()))
                && interestRate.errorMessage == null && interestRate.value.isNotBlank()
//                && paymentDueDate.errorMessage == null && paymentDueDate.value.isNotBlank()
//                && paymentPer.errorMessage == null && paymentPer.value.isNotBlank()
                && !(type.value != "cc" && paymentPer == 0.0)
                && !(type.value == "cc" && (creditLimit.errorMessage != null || creditLimit.value.isBlank()))
    }

    var expandedType by remember { mutableStateOf(false) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val customers by customerRepository.customers.collectAsState()

    val customer = customers.find { it.ssn == ssn }

    val iconType = if (expandedType)
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
                            "ls" -> "Short Term"
                            "ll" -> "Long Term"
                            "cc" -> "Credit Card"
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
                        listOf("ls", "ll", "cc").forEach { loanType ->
                            DropdownMenuItem(
                                text = { Text(
                                    when (loanType) {
                                        "ls" -> "Short Term"
                                        "ll" -> "Long Term"
                                        "cc" -> "Credit Card"
                                        else -> ""
                                    })
                                },
                                onClick = {
                                    type = type.copy(value = loanType, errorMessage = InputValidator.getLoanTypeErrorOrNull(loanType))
                                    expandedType = false
                                },
                                modifier = Modifier.width(with(LocalDensity.current){textFieldSize.width.toDp()})
                            )
                        }
                    }
                }

                if (type.value.isNotBlank()) {
                    if (type.value != "cc") {
                        CustomTextField(
                            label = "Amount",
                            inputWrapper = balance,
                            visualTransformation = CurrencyAmountInputVisualTransformation(),
                            onValueChange = {
                                if (it.all { character -> character.isDigit() }) {
                                    balance = balance.copy(value = it, errorMessage = getBalanceErrorOrNull(it))
                                }
                            }
                        )
                        CustomTextField(
                            label = "Number Of Years",
                            inputWrapper = length,
                            onValueChange = {
                                if (it.all { character -> character.isDigit() }) {
                                    length = length.copy(value = it, errorMessage = getLengthErrorOrNull(it))
                                }
                            }
                        )
                    }
                    CustomTextField(
                        label = "Interest Rate",
                        inputWrapper = interestRate,
                        visualTransformation = InterestInputVisualTransformation(),
                        onValueChange = {
                            if (it.all { character -> character.isDigit() }) {
                                interestRate = interestRate.copy(value = it, errorMessage = getInterestRateErrorOrNull(it))
                            }
                        }
                    )
//                    CustomTextField(
//                        label = "Date Payment Due",
//                        inputWrapper = paymentDueDate,
//                        visualTransformation = DateInputVisualTransformation(),
//                        onValueChange = {
//                            if (it.all { character -> character.isDigit() } && it.length < 9) {
//                                paymentDueDate = paymentDueDate.copy(value = it, errorMessage = getDueDateErrorOrNull(it))
//                            }
//                        }
//                    )
                    if (type.value != "cc") {
                        TextField(
                            value = convertDateToString(paymentDueDate),
                            label = { Text("Date Payment Due") },
                            onValueChange = {},
                            enabled = false
                        )
//                        CustomTextField(
//                            label = "Payment Amount",
//                            inputWrapper = paymentPer,
//                            visualTransformation = CurrencyAmountInputVisualTransformation(),
//                            onValueChange = {
//                                if (it.all { character -> character.isDigit() }) {
//                                    paymentPer = paymentPer.copy(value = it, errorMessage = getAmountDueErrorOrNull(it))
//                                }
//                            }
//                        )
                        TextField(
                            value = paymentPer.formatAsMoney(),
                            label = { Text("Payment Amount") },
                            onValueChange = {},
                            enabled = false,
                            isError = paymentPer == 0.0
                        )
                    }
                    if (type.value == "cc") {
                        CustomTextField(
                            label = "Credit Limit",
                            inputWrapper = creditLimit,
                            visualTransformation = CurrencyAmountInputVisualTransformation(),
                            onValueChange = {
                                if (it.all { character -> character.isDigit() }) {
                                    creditLimit = creditLimit.copy(value = it, errorMessage = getBalanceErrorOrNull(it))
                                }
                            }
                        )
                    }
                }

                Button(
                    onClick = {
                        try {
                            val loan: Loan? = when(type.value) {
                                "ls" -> ShortTermLoan("", ssn, (balance.value.toDouble()/100), interestRate.value.toDouble()/10000, paymentDueDate, LocalDate.now(), paymentPer, LocalDate.now(), false)
                                "ll" -> MortgageLoan("", ssn, (balance.value.toDouble()/100), interestRate.value.toDouble()/10000, paymentDueDate, LocalDate.now(), paymentPer, LocalDate.now(), false)
                                "cc" -> CreditCardLoan("", ssn, 0.0, interestRate.value.toDouble()/10000, paymentDueDate, LocalDate.now(), 0.0, LocalDate.now(), false, creditLimit.value.toDouble()/100)
                                else -> null
                            }

                            if (loan != null && loanRepository.addLoan(loan)) {
                                onCreate(ssn, loan.accountNumber)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = inputsValid()
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