package edu.missouriwestern.csc406team1.screens.customer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.model.Customer
import edu.missouriwestern.csc406team1.util.CustomTextField
import edu.missouriwestern.csc406team1.util.InputValidator
import edu.missouriwestern.csc406team1.util.InputWrapper
import edu.missouriwestern.csc406team1.util.isValidAddressOrCity

@Composable
fun CustomerAccountCreationScreen(customerRepository: CustomerRepository, onClickCreate: (String) -> Unit, onBack: () -> Unit) {

    var ssn by remember { mutableStateOf(InputWrapper()) }
    var firstName by remember { mutableStateOf(InputWrapper()) }
    var lastName by remember { mutableStateOf(InputWrapper()) }
    var address by remember { mutableStateOf(InputWrapper()) }
    var city by remember { mutableStateOf(InputWrapper()) }
    var state by remember { mutableStateOf(InputWrapper()) }
    var zipcode by remember { mutableStateOf(InputWrapper()) }

    fun inputsValid(): Boolean {
        return ssn.errorMessage == null && ssn.value.isNotBlank()
                && firstName.errorMessage == null && firstName.value.isNotBlank()
                && lastName.errorMessage == null && lastName.value.isNotBlank()
                && address.errorMessage == null && address.value.isNotBlank()
                && city.errorMessage == null && city.value.isNotBlank()
                && state.errorMessage == null && state.value.isNotBlank()
                && zipcode.errorMessage == null && zipcode.value.isNotBlank()
    }

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text("Back")
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomTextField(
                label = "SSN",
                inputWrapper = ssn,
                onValueChange = {
                    if (it.all { character -> character.isDigit() } && it.length <= 9) {
                        ssn = ssn.copy(value = it, errorMessage = InputValidator.getSSNErrorOrNull(it))
                    }
                }
            )

            CustomTextField(
                label = "First Name",
                inputWrapper = firstName,
                onValueChange = {
                    if (it.all { character -> character.isLetter() }) {
                        firstName =
                            firstName.copy(value = it, errorMessage = InputValidator.getFirstNameErrorOrNull(it))
                    }
                }
            )

            CustomTextField(
                label = "Last Name",
                inputWrapper = lastName,
                onValueChange = {
                    if (it.all { character -> character.isLetter() }) {
                        lastName = lastName.copy(value = it, errorMessage = InputValidator.getLastNameErrorOrNull(it))
                    }
                }
            )

            CustomTextField(
                label = "Address",
                inputWrapper = address,
                onValueChange = {
                    if (it.isValidAddressOrCity()) {
                        address = address.copy(value = it, errorMessage = InputValidator.getAddressErrorOrNull(it))
                    }
                }
            )

            CustomTextField(
                label = "City",
                inputWrapper = city,
                onValueChange = {
                    if (it.isValidAddressOrCity()) {
                        city = city.copy(value = it, errorMessage = InputValidator.getCityErrorOrNull(it))
                    }
                }
            )

            CustomTextField(
                label = "State",
                inputWrapper = state,
                onValueChange = {
                    if (it.all { character -> character.isLetter() } && it.length <= 2) {
                        state =
                            state.copy(value = it.uppercase(), errorMessage = InputValidator.getStateErrorOrNull(it))
                    }
                }
            )

            CustomTextField(
                label = "Postal Code",
                inputWrapper = zipcode,
                onValueChange = {
                    if (it.all { character -> character.isDigit() } && it.length <= 5) {
                        zipcode = zipcode.copy(value = it, errorMessage = InputValidator.getZipcodeErrorOrNull(it))
                    }
                }
            )

            Button(
                onClick = {
                    onBack()
                    val customer = Customer(
                        ssn.value,
                        address.value,
                        city.value,
                        state.value,
                        zipcode.value,
                        firstName.value,
                        lastName.value
                    )
                    customerRepository.addCustomer(customer)
                    onClickCreate(ssn.value)
                },
                modifier = Modifier.align(Alignment.End),
                enabled = inputsValid()
            ) {
                Text("Create")
            }
        }
    }
}