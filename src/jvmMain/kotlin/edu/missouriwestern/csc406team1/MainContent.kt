import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import edu.missouriwestern.csc406team1.database.CustomerRepository
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
import edu.missouriwestern.csc406team1.screens.CreateAccountScreen
import edu.missouriwestern.csc406team1.screens.LoginScreen
import edu.missouriwestern.csc406team1.screens.customer.*
import edu.missouriwestern.csc406team1.screens.manager.ManagerStartScreen
import edu.missouriwestern.csc406team1.screens.teller.TellerStartScreen
import edu.missouriwestern.csc406team1.viewmodel.customer.LoginScreenViewModel

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
            is Screen.Login -> LoginScreen(onClickCustomer = { navigation.push(Screen.CustomerLogin) }, onClickBankTeller = { navigation.push(Screen.TellerStart) }, onClickBankManager = { navigation.push(Screen.ManagerStart) })

            is Screen.CustomerLogin -> CustomerLoginScreen(loginScreenViewModel = LoginScreenViewModel(customerRepository), onClickLogin = { navigation.push(Screen.CustomerSelectBankAccount(it)) }, onBack = navigation::pop)
            is Screen.CustomerSelectBankAccount -> CustomerSelectBankAccountScreen(customerRepository = customerRepository, accountRepository = accountRepository, customerSSN = screen.ssn, onClickAccount = { ssn, id -> navigation.push(Screen.CustomerBankAccountDetails(ssn, id)) }, onBack = navigation::pop)
            is Screen.CustomerBankAccountDetails -> CustomerBankAccountDetailsScreen(customerRepository = customerRepository, accountRepository = accountRepository, customerSSN = screen.ssn, accountId = screen.id, onBack = navigation::pop, onTransfer = { ssn, id -> navigation.push(Screen.CustomerTransferMoney(ssn, id)) }, onWithdraw = {}, onDeposit = {})
            is Screen.CustomerTransferMoney -> CustomerTransferMoneyScreen(customerRepository = customerRepository, accountRepository = accountRepository, customerSSN = screen.ssn, accountId = screen.id, onBack = navigation::pop)
            is Screen.CustomerWithdrawMoney -> {}
            is Screen.CustomerDepositMoney -> {}

            is Screen.TellerStart -> TellerStartScreen(onBack = navigation::pop)

            is Screen.ManagerStart -> ManagerStartScreen(onBack = navigation::pop)

            is Screen.AccountCreation -> CreateAccountScreen(customerRepository = customerRepository, onClickCreate = { navigation.push(Screen.CustomerSelectBankAccount(it)) }, onBack = navigation::pop)
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
    object AccountCreation : Screen()

    @Parcelize
    object CustomerLogin : Screen()

    @Parcelize
    data class CustomerSelectBankAccount(val ssn: String) : Screen()

    @Parcelize
    data class CustomerBankAccountDetails(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class CustomerTransferMoney(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class CustomerWithdrawMoney(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class CustomerDepositMoney(val ssn: String, val id: String) : Screen()

    @Parcelize
    object TellerStart : Screen()

    @Parcelize
    object ManagerStart : Screen()

}