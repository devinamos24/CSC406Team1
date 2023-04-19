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
import edu.missouriwestern.csc406team1.database.TransactionRepository
import edu.missouriwestern.csc406team1.screens.CreateAccountScreen
import edu.missouriwestern.csc406team1.screens.LoginScreen
import edu.missouriwestern.csc406team1.screens.customer.*
import edu.missouriwestern.csc406team1.screens.manager.*
import edu.missouriwestern.csc406team1.screens.teller.*


/**
 * This Composable is responsible for managing the stack.
 * The stack is essentially the chain of windows opened.
 * By keeping track of them, we allow the user to go back.
 */
@Composable
fun MainContent(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    transactionRepository: TransactionRepository,
) {
    val navigation = remember { StackNavigation<Screen>() }

    ChildStack(
        source = navigation,
        initialStack = { listOf(Screen.Login) },
        animation = stackAnimation(fade() + scale()),
    ) { screen ->
        when (screen) {
            is Screen.Login -> LoginScreen(onClickCustomer = { navigation.push(Screen.CustomerLogin) }, onClickBankTeller = { navigation.push(Screen.TellerStart) }, onClickBankManager = { navigation.push(Screen.ManagerStart) })
            //Customer
            is Screen.CustomerLogin -> CustomerLoginScreen(customerRepository = customerRepository, onClickLogin = { navigation.push(Screen.CustomerSelectBankAccount(it)) }, onBack = navigation::pop)
            is Screen.CustomerSelectBankAccount -> CustomerSelectBankAccountScreen(customerRepository = customerRepository, accountRepository = accountRepository, customerSSN = screen.ssn, onClickAccount = { ssn, id -> navigation.push(Screen.CustomerBankAccountDetails(ssn, id)) }, onBack = navigation::pop)
            is Screen.CustomerBankAccountDetails -> CustomerBankAccountDetailsScreen(customerRepository = customerRepository, accountRepository = accountRepository, customerSSN = screen.ssn, accountId = screen.id, onBack = navigation::pop, onTransfer = { ssn, id -> navigation.push(Screen.CustomerTransferMoney(ssn, id)) }, onWithdraw = {}, onDeposit = {})
            is Screen.CustomerTransferMoney -> CustomerTransferMoneyScreen(customerRepository = customerRepository, accountRepository = accountRepository, customerSSN = screen.ssn, accountId = screen.id, onBack = navigation::pop)
            is Screen.CustomerWithdrawMoney -> {}
            is Screen.CustomerDepositMoney -> {}
            //Teller
            is Screen.TellerStart -> TellerStartScreen(onBack = navigation::pop, onListAccounts = {navigation.push(Screen.TellerSearchCustomer)})
            is Screen.TellerCreateCustomer -> CreateAccountScreen(customerRepository = customerRepository, onClickCreate = {}, onBack = navigation::pop)
            is Screen.TellerSearchCustomer -> TellerSearchCustomerScreen(customerRepository = customerRepository, onClickCustomer = { navigation.push(Screen.TellerEditCustomer(it)) }, onBack = navigation::pop)
            is Screen.TellerEditCustomer -> TellerEditCustomerScreen(customerRepository = customerRepository, accountRepository = accountRepository, ssn = screen.ssn, onClickAccount = { ssn, id -> navigation.push(Screen.TellerEditCustomerBankAccount(ssn, id)) }, onBack = navigation::pop)
            is Screen.TellerEditCustomerBankAccount -> TellerEditCustomerBankAccountScreen(customerRepository = customerRepository, accountRepository = accountRepository, transactionRepository = transactionRepository, ssn = screen.ssn, id = screen.id, onCreditAccount = { ssn, id -> navigation.push(Screen.TellerCreditCustomerBankAccount(ssn = ssn, id = id)) }, onDebitAccount = { ssn, id -> navigation.push(Screen.TellerDebitCustomerBankAccount(ssn = ssn, id = id)) }, onTransferMoney = {ssn, id -> navigation.push(Screen.TellerTransferMoney(ssn = ssn, id = id))} , onBack = navigation::pop)
            is Screen.TellerCreditCustomerBankAccount -> TellerCreditCustomerBankAccountScreen(customerRepository = customerRepository, accountRepository = accountRepository, transactionRepository = transactionRepository, ssn = screen.ssn, id = screen.id, onBack = navigation::pop)
            is Screen.TellerDebitCustomerBankAccount -> TellerDebitCustomerBankAccountScreen(customerRepository = customerRepository, accountRepository = accountRepository, transactionRepository = transactionRepository, ssn = screen.ssn, id = screen.id, onBack = navigation::pop)
            is Screen.TellerTransferMoney -> TellerTransferMoneyScreen(customerRepository = customerRepository, accountRepository = accountRepository, customerSSN = screen.ssn, accountId = screen.id, onBack = navigation::pop)
            //Manager
            is Screen.ManagerStart -> ManagerStartScreen(onCreateCustomer = { navigation.push(Screen.ManagerCreateCustomer) }, onEditCustomerData = { navigation.push(Screen.ManagerSearchCustomer) },onBack = navigation::pop)
            is Screen.ManagerCreateCustomer -> CreateAccountScreen(customerRepository = customerRepository, onClickCreate = { ssn -> navigation.pop().also { navigation.push(Screen.ManagerSearchCustomer) }.also { navigation.push(Screen.ManagerEditCustomer(ssn)) } }, onBack = navigation::pop)
            is Screen.ManagerSearchCustomer -> ManagerSearchCustomerScreen(customerRepository = customerRepository, onClickCustomer = { navigation.push(Screen.ManagerEditCustomer(it)) }, onBack = navigation::pop)
            is Screen.ManagerEditCustomer -> ManagerEditCustomerScreen(customerRepository = customerRepository, accountRepository = accountRepository, ssn = screen.ssn, onClickAccount = { ssn, id -> navigation.push(Screen.ManagerEditCustomerBankAccount(ssn, id)) }, onClickOpenAccount = { navigation.push(Screen.ManagerCreateCustomerBankAccount(it)) }, onBack = navigation::pop)
            is Screen.ManagerCreateCustomerBankAccount -> ManagerCreateCustomerBankAccount(customerRepository = customerRepository, accountRepository = accountRepository, ssn = screen.ssn, onCreate = {}, onBack = navigation::pop)
            is Screen.ManagerEditCustomerBankAccount -> ManagerEditCustomerBankAccountScreen(customerRepository = customerRepository, accountRepository = accountRepository, transactionRepository = transactionRepository, ssn = screen.ssn, id = screen.id, onCreditAccount = { ssn, id -> navigation.push(Screen.ManagerCreditCustomerBankAccount(ssn = ssn, id = id)) }, onDebitAccount = { ssn, id -> navigation.push(Screen.ManagerDebitCustomerBankAccount(ssn = ssn, id = id)) }, onModifyInterest = { ssn, id -> navigation.push(Screen.ManagerModifyInterestCustomerBankAccount(ssn = ssn, id = id)) }, onViewTransactions = { ssn, id -> navigation.push(Screen.ManagerViewTransactionHistory(ssn = ssn, id = id)) }, onBack = navigation::pop)
            is Screen.ManagerCreditCustomerBankAccount -> ManagerCreditCustomerBankAccountScreen(customerRepository = customerRepository, accountRepository = accountRepository, transactionRepository = transactionRepository, ssn = screen.ssn, id = screen.id, onBack = navigation::pop)
            is Screen.ManagerDebitCustomerBankAccount -> ManagerDebitCustomerBankAccountScreen(customerRepository = customerRepository, accountRepository = accountRepository, transactionRepository = transactionRepository, ssn = screen.ssn, id = screen.id, onBack = navigation::pop)
            is Screen.ManagerModifyInterestCustomerBankAccount -> ManagerModifyInterestCustomerBankAccountScreen(onBack = navigation::pop)
            is Screen.ManagerViewTransactionHistory -> ManagerViewTransactionHistoryScreen(customerRepository = customerRepository, accountRepository = accountRepository, transactionRepository = transactionRepository, ssn = screen.ssn, id = screen.id, onBack = navigation::pop)

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
    //Teller
    @Parcelize
    object TellerStart : Screen()

    @Parcelize
    object TellerCreateCustomer : Screen()

    @Parcelize
    object TellerSearchCustomer : Screen()

    @Parcelize
    data class TellerEditCustomer(val ssn: String) : Screen()

    @Parcelize
    data class TellerEditCustomerBankAccount(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class TellerCreditCustomerBankAccount(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class TellerDebitCustomerBankAccount(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class TellerTransferMoney(val ssn: String, val id: String) : Screen()
    //Manager
    @Parcelize
    object ManagerStart : Screen()

    @Parcelize
    object ManagerCreateCustomer : Screen()

    @Parcelize
    object ManagerSearchCustomer : Screen()

    @Parcelize
    data class ManagerEditCustomer(val ssn: String) : Screen()

    @Parcelize
    data class ManagerCreateCustomerBankAccount(val ssn: String) : Screen()

    @Parcelize
    data class ManagerEditCustomerBankAccount(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class ManagerCreditCustomerBankAccount(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class ManagerDebitCustomerBankAccount(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class ManagerModifyInterestCustomerBankAccount(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class ManagerViewTransactionHistory(val ssn: String, val id: String): Screen()

}