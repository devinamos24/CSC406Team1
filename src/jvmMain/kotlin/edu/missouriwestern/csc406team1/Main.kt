import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.application
import edu.missouriwestern.csc406team1.common.LocalAppResources
import edu.missouriwestern.csc406team1.common.rememberAppResources
import edu.missouriwestern.csc406team1.rememberApplicationState

/**
 * This is our main function for entry into our app
 */
fun main() = application {
    CompositionLocalProvider(LocalAppResources provides rememberAppResources()) {
        BankApplication(rememberApplicationState())
    }
}