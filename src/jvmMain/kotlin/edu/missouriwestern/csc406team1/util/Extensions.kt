package edu.missouriwestern.csc406team1.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import edu.missouriwestern.csc406team1.ArrayListFlow
import edu.missouriwestern.csc406team1.database.model.account.*
import edu.missouriwestern.csc406team1.database.model.loan.CreditCardLoan
import edu.missouriwestern.csc406team1.database.model.loan.Loan
import edu.missouriwestern.csc406team1.database.model.loan.MortgageLoan
import edu.missouriwestern.csc406team1.database.model.loan.ShortTermLoan
import java.text.NumberFormat
import java.util.*

/**
 * This is an extension function for collecting state from our modified arraylist
 */
@Composable
fun <T> ArrayListFlow<T>.collectAsState(): State<List<T>> {
    return flow.collectAsState().apply { toList() }
}

fun String.isValidAddressOrCity(): Boolean {
    return this.all { it.isDigit() || it.isLetter() || it in ".'-#@%&/ " }
}

fun Double.formatAsMoney(): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    format.currency = Currency.getInstance("USD")
    return format.format(this)
}

fun Account.getName(): String {
    return when (this) {
        is CDAccount -> "CD Savings"
        is SavingsAccount -> "Standard Savings"
        is TMBAccount -> "TMB Checking"
        is GoldDiamondAccount -> "Gold/Diamond Checking"
        else -> "Account" } + ", Date Opened: ${DateConverter.convertDateToString(this.dateOpened)}"
}

fun Loan.getName(): String {
    return when (this) {
        is MortgageLoan -> "Mortgage Loan"
        is ShortTermLoan -> "Short Term Loan"
        is CreditCardLoan -> "Credit Card Loan"
        else -> "Loan"
    } + ", Date Opened: ${DateConverter.convertDateToString(this.dateOpened)}"
}