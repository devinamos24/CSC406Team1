package edu.missouriwestern.csc406team1.window

import MainContent
import ProvideComponentContext
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import edu.missouriwestern.csc406team1.common.LocalAppResources
import edu.missouriwestern.csc406team1.theme.AppTheme
import kotlinx.coroutines.launch
import edu.missouriwestern.csc406team1.util.YesNoCancelDialog

/**
 * This composable creates a window starting at our entry screen
 */
@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun BankWindow(state: BankWindowState) {
    // Remember our scope for our coroutines between compositions
    val scope = rememberCoroutineScope()

    // If we want to exit, launch a coroutine to do it so the other windows don't get hung up
    fun exit() = scope.launch { state.exit() }

    // The Window composable represents an entire window
    Window(
        state = state.window,
        title = "Banking App",
        icon = LocalAppResources.current.icon,
        onCloseRequest = { exit() }
    ) {

        // This controller helps keep track of our position on the stack
        LifecycleController(state.lifecycle, state.window)

        // This simply renders our menu bar
        WindowMenuBar(state)

        // This section represents the core of the app, see the Screens.Kt file to learn more
        Surface(modifier = Modifier.fillMaxSize()) {
            AppTheme {
                CompositionLocalProvider(LocalScrollbarStyle provides defaultScrollbarStyle()) {
                    ProvideComponentContext(state.rootComponentContext) {
                        MainContent(customerRepository = state.customerRepository, accountRepository = state.accountRepository, transactionRepository = state.transactionRepository)
                    }
                }
            }
        }

        // If we are exiting the program, wait for the user to select a dialog option
        if (state.exitDialog.isAwaiting) {
            YesNoCancelDialog(
                title = "Banking",
                message = "Save changes?",
                onResult = { state.exitDialog.onResult(it) }
            )
        }
    }
}

/**
 * This Composable represents our menu bar at the top of each window
 */
@Composable
private fun FrameWindowScope.WindowMenuBar(state: BankWindowState) = MenuBar {
    Menu("File") {
        Item("New window", onClick = state::newWindow)
    }
}