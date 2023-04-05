package edu.missouriwestern.csc406team1.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText

fun socialSecurityNumberFilter(text: AnnotatedString): TransformedText {

    // Making XXXXXXXXX string.
    val trimmed = (if (text.text.length >= 10) text.text.substring(0..9) else text.text)
        .filter { it.isDigit() }

    val socialSecurityNumberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 10) return offset
            return 10
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 10) return offset
            return 10
        }
    }

    return TransformedText(AnnotatedString(trimmed), socialSecurityNumberOffsetTranslator)
}