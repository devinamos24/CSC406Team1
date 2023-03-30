import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.missouriwestern.csc406team1.CustomerRepository
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import edu.missouriwestern.csc406team1.database.AccountRepository
import edu.missouriwestern.csc406team1.database.Customer
import edu.missouriwestern.csc406team1.util.collectAsState

/**
 * This screen is where the user will select who they are
 */
@Composable
fun LoginScreen(onClickCustomer: () -> Unit, onClickBankTeller: () -> Unit, onClickBankManager: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Text(
                text = "Who are you?",
                )
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(onClick = onClickCustomer) {
                    Text("Customer")
                }
                Button(onClick = onClickBankTeller) {
                    Text("Bank Teller")
                }
                Button(onClick = onClickBankManager) {
                    Text("Bank Manager")
                }
            }
        }
    }
}

/**
 * This Composable represents a customer selection in the list of customers
 * onClick sends the customer to their details page
 */
@Composable
fun CustomerButton(customer: Customer, onClick: () -> Unit) {
    Box(
        modifier = Modifier.height(32.dp)
            .fillMaxWidth()
            .background(color = Color(0, 0, 0, 20))
            .padding(start = 10.dp)
            .clickable {onClick()},
        contentAlignment = Alignment.CenterStart
    ) {
        val text = "${customer.firstname} ${customer.lastname} from ${customer.city}, ${customer.state}"
        Text(text = text)
    }
}

/**
 * This screen is where the customer will choose their account
 * TODO: Maybe add an option for the customer to search their ssn?
 */
@Composable
fun CustomerSelectionScreen(customerRepository: CustomerRepository, onBack: () -> Unit, onClickCustomer: (customer: Customer) -> Unit) {
    val customers by customerRepository.customers.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
                Button(
                    onClick = onBack,
                ) {
                    Text("Back")
                }
                Button(
                    onClick = {
                        customerRepository.addCustomer(Customer("423453244","114 North 4th","Clarksdale","MO","64493","Ronald","Jones"))
                    }
                ) {
                    Text("Add customer")
                }
            }
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(10.dp)
            ) {

                val state = rememberLazyListState()

                LazyColumn(Modifier.fillMaxSize().padding(end = 12.dp).align(Alignment.Center), state) {
                    customers.sorted().forEach {
                        item {
                            CustomerButton(
                                customer = it,
                                onClick = { onClickCustomer(it) }
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }
                }
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(
                        scrollState = state
                    )
                )
            }
        }
    }
}

/**
 * This screen will be where the customer sees their different accounts and such
 */
@Composable
fun CustomerDetailsScreen(customer: Customer, customerRepository: CustomerRepository, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text("Back")
        }
    }
}

/**
 * This Composable is responsible for managing the stack.
 * The stack is essentially the chain of windows opened.
 * By keeping track of them, we allow the user to go back.
 */
@Composable
fun MainContent(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository
) {
    val navigation = remember { StackNavigation<Screen>() }

    ChildStack(
        source = navigation,
        initialStack = { listOf(Screen.Login) },
        animation = stackAnimation(fade() + scale()),
    ) { screen ->
        when (screen) {
            is Screen.Login -> LoginScreen(onClickCustomer = { navigation.push(Screen.CustomerSelection) }, onClickBankTeller = {}, onClickBankManager = {})
            is Screen.CustomerSelection -> CustomerSelectionScreen(customerRepository = customerRepository, onBack = navigation::pop, onClickCustomer = { navigation.push(Screen.CustomerDetails(customer = it))})
            is Screen.CustomerDetails -> CustomerDetailsScreen(customer = screen.customer, customerRepository = customerRepository, onBack = navigation::pop)
        }
    }
}

/**
 * This sealed class represents each screen and the parameters it requires
 * Think of a sealed class like a fancy enum :)
 */
sealed class Screen : Parcelable {

    @Parcelize
    object Login : Screen()

    @Parcelize
    object CustomerSelection : Screen()

    @Parcelize
    data class CustomerDetails(val customer: Customer) : Screen()
}