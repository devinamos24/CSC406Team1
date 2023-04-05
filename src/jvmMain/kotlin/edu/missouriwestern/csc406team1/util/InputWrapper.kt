package edu.missouriwestern.csc406team1.util

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
data class InputWrapper(
    val value: String = "",
    val errorMessage: String? = null
) : Parcelable