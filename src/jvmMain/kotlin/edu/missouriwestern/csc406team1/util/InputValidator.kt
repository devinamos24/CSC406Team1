package edu.missouriwestern.csc406team1.util

import java.time.LocalDate
import java.time.format.DateTimeParseException

object InputValidator {

    fun getSSNErrorOrNull(input: String): String? {
        return when {
            input.length < 9 -> "SSN is too short"
            !input.all { it.isDigit() } -> "SSN can only contain numbers"
            else -> null
        }
    }

    fun getAddressErrorOrNull(input: String): String? {
        return when {
            input.isEmpty() -> "Address is too short"
            else -> null
        }
    }

    fun getCityErrorOrNull(input: String): String? {
        return when {
            input.isEmpty() -> "City is too short"
            else -> null
        }
    }

    fun getStateErrorOrNull(input: String): String? {
        return when {
            input.length < 2 -> "State is too short"
            !input.all { it.isLetter() } -> "State can only contain letters"
            else -> null
        }
    }

    fun getZipcodeErrorOrNull(input: String): String? {
        return when {
            input.length < 5 -> "Postal Code is too short"
            !input.all { it.isDigit() } -> "Postal Code can only contain numbers"
            else -> null
        }
    }

    fun getFirstNameErrorOrNull(input: String): String? {
        return when {
            input.isEmpty() -> "First name is too short"
            !input.all { it.isLetter() } -> "First name can only contain letters"
            else -> null
        }
    }

    fun getLastNameErrorOrNull(input: String): String? {
        return when {
            input.isEmpty() -> "Last name is too short"
            !input.all { it.isLetter() } -> "Last name can only contain letters"
            else -> null
        }
    }

    fun getAccountTypeErrorOrNull(input: String): String? {
        return when {
            input !in listOf("CD", "S", "TMB", "GD") -> "Unrecognized account type"
            else -> null
        }
    }

    fun getLoanTypeErrorOrNull(input: String): String? {
        return when {
            input !in listOf("ls", "ll") -> "Unrecognized loan type"
            else -> null
        }
    }

    fun getDueDateErrorOrNull(input: String): String? {
        val date: LocalDate
        try {
            date = DateConverter.convertStringToDateInterface(input)
        } catch (e: DateTimeParseException) {
            return "Date format not recognized or date is invalid"
        }

        return when {
            date.isBefore(LocalDate.now()) -> "Due date must not be in the past"
            else -> null
        }
    }

    fun getInterestRateErrorOrNull(input: String): String? {
        val interestRate: Double
        try {
            interestRate = input.toDouble() / 10000
        } catch (e: NumberFormatException) {
            return "Interest rate format is invalid"
        }
        return when {
            interestRate >= 1.0 -> "Interest rate must be below 100%"
            else -> null
        }
    }


    fun getAmountDueErrorOrNull(input: String): String? {
        val amount: Double
        try {
            amount = input.toDouble() / 100
        } catch (e: NumberFormatException) {
            return "Amount due format is invalid"
        }
        return when {
            amount == 0.0 -> "Amount due must be above $0"
            else -> null
        }
    }

    fun getBalanceErrorOrNull(input: String): String? {
        val amount: Double
        try {
            amount = input.toDouble() / 100
        } catch (e: NumberFormatException) {
            return "Amount format is invalid"
        }
        return when {
            amount == 0.0 -> "Amount must be above $0"
            else -> null
        }
    }

    fun getInitialBalanceErrorOrNull(input: String): String? {
        val amount: Double
        try {
            amount = input.toDouble() / 100
        } catch (e: NumberFormatException) {
            return "Initial balance format is invalid"
        }
        return when {
            amount < 50.0 -> "Initial balance must be $50 or above"
            else -> null
        }
    }

    fun getCdDueDateErrorOrNull(input: String): String? {
        val date: LocalDate
        try {
            date = DateConverter.convertStringToDateInterface(input)
        } catch (e: DateTimeParseException) {
            return "Date format not recognized or date is invalid"
        }

        return when {
            date.isBefore(LocalDate.now()) -> "Due date must not be in the past"
            else -> null
        }
    }
}