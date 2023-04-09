package edu.missouriwestern.csc406team1.window

import edu.missouriwestern.csc406team1.BankApplicationState
import androidx.compose.ui.window.WindowState
import edu.missouriwestern.csc406team1.util.AlertDialogResult
import edu.missouriwestern.csc406team1.DialogState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

/**
 * This class holds our state for each window
 */
class BankWindowState(
    private val application: BankApplicationState,
    val exitDialog: DialogState<AlertDialogResult>,
    private val exit: suspend (BankWindowState) -> Unit
    ) {

    // A simple window state for things like minimized, maximized, etc.
    val window = WindowState()

    // These pieces help keep track of our position on the screen stack
    val lifecycle = LifecycleRegistry()
    val rootComponentContext = DefaultComponentContext(lifecycle = lifecycle)

    // This is our repository of customer data in memory
    val customerRepository = application.customerRepository
    val accountRepository = application.accountRepository
    val transactionRepository = application.transactionRepository
    val loanRepository = application.loanRepository

    // This function is called when a new window is requested
    fun newWindow() {
        application.newWindow()
    }

    // This function is called when we close a window
    suspend fun exit() {
        exit(this)
    }

    // Both of the above functions send their calls up to the application state
}