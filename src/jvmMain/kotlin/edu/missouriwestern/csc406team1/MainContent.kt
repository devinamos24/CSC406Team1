package edu.missouriwestern.csc406team1

import ChildStack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.WindowScope
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
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.LoanRepository
import edu.missouriwestern.csc406team1.database.TransactionRepository
import edu.missouriwestern.csc406team1.screens.CreateAccountScreen
import edu.missouriwestern.csc406team1.screens.LoginScreen
import edu.missouriwestern.csc406team1.screens.customer.*
import edu.missouriwestern.csc406team1.screens.manager.*
import edu.missouriwestern.csc406team1.screens.teller.*
import edu.missouriwestern.csc406team1.viewmodel.customer.*
import edu.missouriwestern.csc406team1.viewmodel.manager.EditCustomerLoanScreenViewModel

/**
 * This Composable is responsible for managing the stack.
 * The stack is essentially the chain of windows opened.
 * By keeping track of them, we allow the user to go back.
 */
@Composable
fun WindowScope.MainContent(
    customerRepository: CustomerRepository,
    accountRepository: AccountRepository,
    loanRepository: LoanRepository,
    transactionRepository: TransactionRepository,
) {
    val navigation = remember { StackNavigation<Screen>() }

    ChildStack(
        source = navigation,
        initialStack = { listOf(Screen.Login) },
        animation = stackAnimation(fade() + scale()),
    ) { screen ->
        when (screen) {
            is Screen.Login -> LoginScreen(
                onClickCustomer = { navigation.push(Screen.CustomerLogin) },
                onClickBankTeller = { navigation.push(Screen.TellerStart) },
                onClickBankManager = { navigation.push(Screen.ManagerStart) })
            //Customer
            is Screen.CustomerLogin -> CustomerLoginScreen(
                loginScreenViewModel = LoginScreenViewModel(
                    customerRepository,
                    { ssn -> navigation.push(Screen.CustomerSelectBankAccount(ssn = ssn)) },
                    navigation::pop
                )
            )

            is Screen.CustomerSelectBankAccount -> CustomerSelectBankAccountScreen(
                selectBankAccountScreenViewModel = SelectBankAccountScreenViewModel(
                    customerRepository,
                    accountRepository,
                    loanRepository,
                    screen.ssn,
                    { ssn, id -> navigation.push(Screen.CustomerBankAccountDetails(ssn, id)) },
                    { ssn, id -> navigation.push(Screen.CustomerLoanDetails(ssn, id)) },
                    { ssn -> navigation.push(Screen.CustomerShopping(ssn)) },
                    navigation::pop
                )
            )

            is Screen.CustomerShopping -> CustomerShoppingScreen(
                shoppingScreenViewModel = ShoppingScreenViewModel(
                    customerRepository,
                    accountRepository,
                    loanRepository,
                    transactionRepository,
                    screen.ssn,
                    navigation::pop
                )
            )

            is Screen.CustomerBankAccountDetails -> CustomerBankAccountDetailsScreen(
                bankAccountScreenViewModel = BankAccountScreenViewModel(
                    customerRepository,
                    accountRepository,
                    screen.ssn,
                    screen.id,
                    { ssn, id -> navigation.push(Screen.CustomerTransferMoney(ssn, id)) },
                    { ssn, id -> navigation.push(Screen.CustomerWithdrawMoney(ssn, id)) },
                    { ssn, id -> navigation.push(Screen.CustomerDepositMoney(ssn, id)) },
                    { ssn, id -> navigation.push(Screen.CustomerViewTransactionHistory(ssn, id)) },
                    { ssn, id -> navigation.push(Screen.CustomerSelectBackupAccount(ssn, id)) },
                    navigation::pop
                )
            )

            is Screen.CustomerTransferMoney -> CustomerTransferMoneyScreen(
                transferMoneyScreenViewModel = TransferMoneyScreenViewModel(
                    customerRepository,
                    accountRepository,
                    transactionRepository,
                    screen.ssn,
                    screen.id,
                    navigation::pop
                ),
            )

            is Screen.CustomerWithdrawMoney -> CustomerWithdrawMoneyScreen(
                withdrawMoneyScreenViewModel = WithdrawMoneyScreenViewModel(
                    customerRepository,
                    accountRepository,
                    transactionRepository,
                    screen.ssn,
                    screen.id,
                    navigation::pop
                )
            )

            is Screen.CustomerDepositMoney -> CustomerDepositMoneyScreen(
                depositMoneyScreenViewModel = DepositMoneyScreenViewModel(
                    customerRepository,
                    accountRepository,
                    transactionRepository,
                    screen.ssn,
                    screen.id,
                    navigation::pop
                )
            )

            is Screen.CustomerSelectBackupAccount -> CustomerSelectBackupAccountScreen(
                selectBackupAccountScreenViewModel = SelectBackupAccountScreenViewModel(
                    customerRepository,
                    accountRepository,
                    screen.ssn,
                    screen.id,
                    navigation::pop
                )
            )

            is Screen.CustomerViewTransactionHistory -> CustomerViewTransactionHistoryScreen(
                viewTransactionHistoryScreenViewModel = ViewTransactionHistoryScreenViewModel(
                    customerRepository,
                    accountRepository,
                    transactionRepository,
                    screen.ssn,
                    screen.id,
                    navigation::pop
                )
            )

            is Screen.CustomerLoanDetails -> CustomerLoanDetailsScreen(
                loanDetailsScreenViewModel = LoanDetailsScreenViewModel(
                    customerRepository,
                    loanRepository,
                    screen.ssn,
                    screen.id,
                    { ssn, id -> navigation.push(Screen.CustomerLoanMakePayment(ssn, id)) },
                    { ssn, id -> navigation.push(Screen.CustomerViewLoanPaymentHistory(ssn, id)) },
                    navigation::pop
                )
            )

            is Screen.CustomerLoanMakePayment -> CustomerLoanMakePaymentScreen(
                loanMakePaymentScreenViewModel = LoanMakePaymentScreenViewModel(
                    customerRepository,
                    accountRepository,
                    transactionRepository,
                    loanRepository,
                    screen.ssn,
                    screen.id,
                    navigation::pop
                )
            )

            is Screen.CustomerViewLoanPaymentHistory -> CustomerViewLoanPaymentHistoryScreen(
                customerRepository = customerRepository,
                loanRepository = loanRepository,
                transactionRepository = transactionRepository,
                ssn = screen.ssn,
                id = screen.id,
                onBack = navigation::pop
            )

            //Teller
            is Screen.TellerStart -> TellerStartScreen(
                onBack = navigation::pop,
                onListAccounts = { navigation.push(Screen.TellerSearchCustomer) })

            is Screen.TellerCreateCustomer -> CreateAccountScreen(
                customerRepository = customerRepository,
                onClickCreate = {},
                onBack = navigation::pop
            )

            is Screen.TellerSearchCustomer -> TellerSearchCustomerScreen(
                customerRepository = customerRepository,
                onClickCustomer = { navigation.push(Screen.TellerEditCustomer(it)) },
                onBack = navigation::pop
            )

            is Screen.TellerEditCustomer -> TellerEditCustomerScreen(
                customerRepository = customerRepository,
                accountRepository = accountRepository,
                ssn = screen.ssn,
                onClickAccount = { ssn, id -> navigation.push(Screen.TellerEditCustomerBankAccount(ssn, id)) },
                onBack = navigation::pop
            )

            is Screen.TellerEditCustomerBankAccount -> TellerEditCustomerBankAccountScreen(
                customerRepository = customerRepository,
                accountRepository = accountRepository,
                transactionRepository = transactionRepository,
                ssn = screen.ssn,
                id = screen.id,
                onCreditAccount = { ssn, id ->
                    navigation.push(
                        Screen.TellerCreditCustomerBankAccount(
                            ssn = ssn,
                            id = id
                        )
                    )
                },
                onDebitAccount = { ssn, id ->
                    navigation.push(
                        Screen.TellerDebitCustomerBankAccount(
                            ssn = ssn,
                            id = id
                        )
                    )
                },
                onTransferMoney = { ssn, id -> navigation.push(Screen.TellerTransferMoney(ssn = ssn, id = id)) },
                onBack = navigation::pop)

            is Screen.TellerCreditCustomerBankAccount -> TellerCreditCustomerBankAccountScreen(
                customerRepository = customerRepository,
                accountRepository = accountRepository,
                transactionRepository = transactionRepository,
                ssn = screen.ssn,
                id = screen.id,
                onBack = navigation::pop
            )

            is Screen.TellerDebitCustomerBankAccount -> TellerDebitCustomerBankAccountScreen(
                customerRepository = customerRepository,
                accountRepository = accountRepository,
                transactionRepository = transactionRepository,
                ssn = screen.ssn,
                id = screen.id,
                onBack = navigation::pop
            )

            is Screen.TellerTransferMoney -> TellerTransferMoneyScreen(
                customerRepository = customerRepository,
                accountRepository = accountRepository,
                transactionRepository = transactionRepository,
                ssn = screen.ssn,
                id = screen.id,
                onBack = navigation::pop
            )
            //Manager
            is Screen.ManagerStart -> ManagerStartScreen(
                onCreateCustomer = { navigation.push(Screen.ManagerCreateCustomer) },
                onEditCustomerData = { navigation.push(Screen.ManagerSearchCustomer) },
                onBack = navigation::pop
            )

            is Screen.ManagerCreateCustomer -> CreateAccountScreen(
                customerRepository = customerRepository,
                onClickCreate = { ssn ->
                    navigation.pop().also { navigation.push(Screen.ManagerSearchCustomer) }
                        .also { navigation.push(Screen.ManagerEditCustomer(ssn)) }
                },
                onBack = navigation::pop
            )

            is Screen.ManagerSearchCustomer -> ManagerSearchCustomerScreen(
                customerRepository = customerRepository,
                onClickCustomer = { navigation.push(Screen.ManagerEditCustomer(it)) },
                onBack = navigation::pop
            )

            is Screen.ManagerEditCustomer -> ManagerEditCustomerScreen(
                customerRepository = customerRepository,
                accountRepository = accountRepository,
                loanRepository = loanRepository,
                ssn = screen.ssn,
                onClickAccount = { ssn, id -> navigation.push(Screen.ManagerEditCustomerBankAccount(ssn, id)) },
                onClickLoan = { ssn, id -> navigation.push(Screen.ManagerEditCustomerLoan(ssn, id)) },
                onClickOpenAccount = { navigation.push(Screen.ManagerCreateCustomerBankAccount(it)) },
                onClickOpenLoan = { navigation.push(Screen.ManagerCreateCustomerLoan(it)) },
                onBack = navigation::pop
            )

            is Screen.ManagerCreateCustomerBankAccount -> ManagerCreateCustomerBankAccountScreen(
                customerRepository = customerRepository,
                accountRepository = accountRepository,
                ssn = screen.ssn,
                onCreate = { ssn, id -> navigation.pop(); navigation.push(Screen.ManagerEditCustomerBankAccount(ssn, id)) },
                onBack = navigation::pop
            )

            is Screen.ManagerCreateCustomerLoan -> ManagerCreateCustomerLoanScreen(
                customerRepository = customerRepository,
                loanRepository = loanRepository,
                ssn = screen.ssn,
                onCreate = { ssn, id -> navigation.pop(); navigation.push(Screen.ManagerEditCustomerLoan(ssn, id)) },
                onBack = navigation::pop
            )

            is Screen.ManagerEditCustomerBankAccount -> ManagerEditCustomerBankAccountScreen(
                customerRepository = customerRepository,
                accountRepository = accountRepository,
                ssn = screen.ssn,
                id = screen.id,
                onCreditAccount = { ssn, id -> navigation.push(Screen.ManagerCreditCustomerBankAccount(ssn = ssn, id = id)) },
                onDebitAccount = { ssn, id -> navigation.push(Screen.ManagerDebitCustomerBankAccount(ssn = ssn, id = id)) },
                onModifyInterest = { ssn, id -> navigation.push(Screen.ManagerModifyInterestCustomerBankAccount(ssn = ssn, id = id)) },
                onViewTransactions = { ssn, id -> navigation.push(Screen.ManagerViewTransactionHistory(ssn = ssn, id = id)) },
                onDeleteAccount = { ssn, id -> navigation.push(Screen.ManagerCloseCustomerBankAccount(ssn = ssn, id = id)) },
                onTransfer = { ssn, id -> navigation.push(Screen.ManagerTransferMoneyCustomerBankAccount(ssn = ssn, id = id)) },
                onBack = navigation::pop
            )

            is Screen.ManagerEditCustomerLoan -> ManagerEditCustomerLoanScreen(
                editCustomerLoanScreenViewModel = EditCustomerLoanScreenViewModel(
                    customerRepository,
                    loanRepository,
                    screen.ssn,
                    screen.id,
                    { ssn, id -> navigation.push(Screen.ManagerCreditCustomerLoan(ssn = ssn, id = id)) },
                    { ssn, id -> navigation.push(Screen.ManagerViewLoanPaymentHistory(ssn = ssn, id = id)) },
                    navigation::pop
                )
            )

            is Screen.ManagerCreditCustomerLoan -> ManagerCreditCustomerLoanScreen(
                customerRepository = customerRepository,
                loanRepository = loanRepository,
                transactionRepository = transactionRepository,
                ssn = screen.ssn,
                id = screen.id,
                onBack = navigation::pop
            )

            is Screen.ManagerCloseCustomerBankAccount -> ManagerCloseCustomerBankAccountScreen(
                customerRepository = customerRepository,
                accountRepository = accountRepository,
                transactionRepository = transactionRepository,
                screen.ssn,
                screen.id,
                navigation::pop
            )

            is Screen.ManagerCreditCustomerBankAccount -> ManagerCreditCustomerBankAccountScreen(
                customerRepository = customerRepository,
                accountRepository = accountRepository,
                transactionRepository = transactionRepository,
                ssn = screen.ssn,
                id = screen.id,
                onBack = navigation::pop
            )

            is Screen.ManagerDebitCustomerBankAccount -> ManagerDebitCustomerBankAccountScreen(
                customerRepository = customerRepository,
                accountRepository = accountRepository,
                transactionRepository = transactionRepository,
                ssn = screen.ssn,
                id = screen.id,
                onBack = navigation::pop
            )

            is Screen.ManagerTransferMoneyCustomerBankAccount -> ManagerTransferMoneyCustomerBankAccountScreen(
                customerRepository = customerRepository,
                accountRepository = accountRepository,
                transactionRepository = transactionRepository,
                screen.ssn,
                screen.id,
                navigation::pop
            )

            is Screen.ManagerModifyInterestCustomerBankAccount -> ManagerEditInterestCustomerBankAccountScreen(onBack = navigation::pop)

            is Screen.ManagerViewTransactionHistory -> ManagerViewTransactionHistoryScreen(
                customerRepository = customerRepository,
                accountRepository = accountRepository,
                transactionRepository = transactionRepository,
                ssn = screen.ssn,
                id = screen.id,
                onBack = navigation::pop
            )

            is Screen.ManagerViewLoanPaymentHistory -> ManagerViewLoanPaymentHistoryScreen(
                customerRepository = customerRepository,
                loanRepository = loanRepository,
                transactionRepository = transactionRepository,
                ssn = screen.ssn,
                id = screen.id,
                onBack = navigation::pop
            )

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
    data class CustomerShopping(val ssn: String) : Screen()

    @Parcelize
    data class CustomerBankAccountDetails(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class CustomerTransferMoney(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class CustomerWithdrawMoney(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class CustomerDepositMoney(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class CustomerSelectBackupAccount(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class CustomerViewTransactionHistory(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class CustomerLoanDetails(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class CustomerLoanMakePayment(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class CustomerViewLoanPaymentHistory(val ssn: String, val id: String) : Screen()

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
    data class ManagerCreateCustomerLoan(val ssn: String) : Screen()

    @Parcelize
    data class ManagerEditCustomerBankAccount(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class ManagerEditCustomerLoan(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class ManagerCloseCustomerBankAccount(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class ManagerCreditCustomerLoan(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class ManagerCreditCustomerBankAccount(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class ManagerDebitCustomerBankAccount(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class ManagerTransferMoneyCustomerBankAccount(val ssn: String, val id: String): Screen()

    @Parcelize
    data class ManagerModifyInterestCustomerBankAccount(val ssn: String, val id: String) : Screen()

    @Parcelize
    data class ManagerViewTransactionHistory(val ssn: String, val id: String): Screen()

    @Parcelize
    data class ManagerViewLoanPaymentHistory(val ssn: String, val id: String) : Screen()

}