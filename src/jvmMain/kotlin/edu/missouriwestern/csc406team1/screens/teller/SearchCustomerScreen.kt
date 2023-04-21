package edu.missouriwestern.csc406team1.screens.teller

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.model.Customer
import edu.missouriwestern.csc406team1.util.*

@Composable
fun TellerSearchCustomerScreen(
    customerRepository: CustomerRepository,
    onClickCustomer: (String) -> Unit,
    onBack: () -> Unit
) {

    val customers by customerRepository.customers.collectAsState()

    var ssn by remember { mutableStateOf(InputWrapper()) }
    var firstName by remember { mutableStateOf(InputWrapper()) }
    var lastName by remember { mutableStateOf(InputWrapper()) }
    var address by remember { mutableStateOf(InputWrapper()) }
    var city by remember { mutableStateOf(InputWrapper()) }
    var state by remember { mutableStateOf(InputWrapper()) }
    var zipcode by remember { mutableStateOf(InputWrapper()) }

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Column {
            Button(
                onClick = onBack,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("Back")
            }

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .padding(10.dp)
                        .weight(0.6f)
                ) {
                    val scrollState = rememberLazyListState()

                    LazyColumn(Modifier.padding(end = 12.dp).align(Alignment.Center), scrollState) {
                        customers.filter {
                            it.ssn.contains(ssn.value, true) &&
                                    it.state.contains(state.value, true) &&
                                    it.city.contains(city.value, true) &&
                                    it.address.contains(address.value, true) &&
                                    it.firstname.contains(firstName.value, true) &&
                                    it.lastname.contains(lastName.value, true) &&
                                    it.zipcode.contains(zipcode.value, true)
                        }.sorted().forEach { customer ->
                            item {
                                CustomerButton(
                                    customer = customer,
                                    onClick = { onClickCustomer(customer.ssn) }
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                        }
                    }
                    VerticalScrollbar(
                        modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                        adapter = rememberScrollbarAdapter(
                            scrollState = scrollState
                        )
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize()
                        .padding(10.dp)
                        .weight(0.4f)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        CustomTextField(
                            label = "SSN",
                            inputWrapper = ssn,
                            onValueChange = {
                                if (it.all { character -> character.isDigit() } && it.length <= 9) {
                                    ssn = ssn.copy(value = it)
                                }
                            }
                        )

                        CustomTextField(
                            label = "First Name",
                            inputWrapper = firstName,
                            onValueChange = {
                                if (it.all { character -> character.isLetter() }) {
                                    firstName =
                                        firstName.copy(value = it)
                                }
                            }
                        )

                        CustomTextField(
                            label = "Last Name",
                            inputWrapper = lastName,
                            onValueChange = {
                                if (it.all { character -> character.isLetter() }) {
                                    lastName = lastName.copy(value = it)
                                }
                            }
                        )

                        CustomTextField(
                            label = "Address",
                            inputWrapper = address,
                            onValueChange = {
                                if (it.isValidAddressOrCity()) {
                                    address = address.copy(value = it)
                                }
                            }
                        )

                        CustomTextField(
                            label = "City",
                            inputWrapper = city,
                            onValueChange = {
                                if (it.isValidAddressOrCity()) {
                                    city = city.copy(value = it)
                                }
                            }
                        )

                        CustomTextField(
                            label = "State",
                            inputWrapper = state,
                            onValueChange = {
                                if (it.all { character -> character.isLetter() } && it.length <= 2) {
                                    state =
                                        state.copy(value = it.uppercase())
                                }
                            }
                        )

                        CustomTextField(
                            label = "Postal Code",
                            inputWrapper = zipcode,
                            onValueChange = {
                                if (it.all { character -> character.isDigit() } && it.length <= 5) {
                                    zipcode = zipcode.copy(value = it)
                                }
                            }
                        )

                        Button(
                            modifier = Modifier.align(Alignment.End),
                            onClick = {
                                ssn = ssn.copy(value = "")
                                firstName = firstName.copy(value = "")
                                lastName = lastName.copy(value = "")
                                address = address.copy(value = "")
                                city = city.copy(value = "")
                                state = state.copy(value = "")
                                zipcode = zipcode.copy(value = "")
                            }
                        ) {
                            Text("Reset")
                        }
                    }

                }
            }
        }


    }
}

@Composable
private fun CustomerButton(
    modifier: Modifier = Modifier,
    customer: Customer,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
            .clickable { onClick() },
    ) {
        customer.run {
            Text("$ssn, $firstname $lastname, $address, $city, $state, $zipcode")
        }
    }
}