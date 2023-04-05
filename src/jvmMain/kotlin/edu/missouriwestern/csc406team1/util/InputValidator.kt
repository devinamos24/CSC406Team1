package edu.missouriwestern.csc406team1.util

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
}