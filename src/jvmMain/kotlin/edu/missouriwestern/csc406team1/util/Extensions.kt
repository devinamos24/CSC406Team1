package edu.missouriwestern.csc406team1.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import edu.missouriwestern.csc406team1.ArrayListFlow
import java.text.NumberFormat
import java.util.Currency

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