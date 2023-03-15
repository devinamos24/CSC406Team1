import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import edu.missouriwestern.csc406team1.common.LocalAppResources
import edu.missouriwestern.csc406team1.window.BankWindow

/**
 * This composable is responsible for creating windows for our list of window states
 */
@Composable
fun ApplicationScope.BankApplication(state: BankApplicationState) {
    for (window in state.windows) {
        key(window) {
            BankWindow(window)
        }
    }

    if (state.windows.isNotEmpty()) {
        ApplicationTray(state)
    }
}

/**
 * This composable is responsible for keeping track of our tray icon if we have one
 * It is also responsible for handling notifications
 */
@Composable
private fun ApplicationScope.ApplicationTray(state: BankApplicationState) {
    Tray(
        LocalAppResources.current.icon,
        state = state.tray,
        tooltip = "Bank App",
    )
}