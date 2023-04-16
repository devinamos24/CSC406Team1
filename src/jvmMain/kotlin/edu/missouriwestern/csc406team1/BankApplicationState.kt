import androidx.compose.runtime.*
import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.TrayState
import edu.missouriwestern.csc406team1.database.CustomerRepository
import edu.missouriwestern.csc406team1.database.CustomerRepositoryImpl
import edu.missouriwestern.csc406team1.database.AccountRepository
import edu.missouriwestern.csc406team1.database.AccountRepositoryImpl
import edu.missouriwestern.csc406team1.database.TransactionRepository
import edu.missouriwestern.csc406team1.database.TransactionRepositoryImpl
import kotlinx.coroutines.*
import edu.missouriwestern.csc406team1.util.AlertDialogResult
import edu.missouriwestern.csc406team1.window.BankWindowState
import java.lang.Exception

/**
 * This composable starts our app with one window and remembers the app state between compositions
 */
@Composable
fun rememberApplicationState() = remember {
    BankApplicationState().apply {
        newWindow()
    }
}

/**
 * This class contains the state of our entire app
 */
class BankApplicationState {

    // The state of our tray, used for sending notifications to OS
    val tray = TrayState()

    // List of window states currently open
    private val _windows = mutableStateListOf<BankWindowState>()
    val windows: List<BankWindowState> get() = _windows

    // A dialog that is created when app is closing
    // Used for asking the user if they want to save
    private val exitDialog = DialogState<AlertDialogResult>()

    // A repository to pull and push customers to
    val customerRepository: CustomerRepository = CustomerRepositoryImpl()

    // A repository to pull and push accounts to
    val accountRepository: AccountRepository = AccountRepositoryImpl()

    // A repository to pull and push transactions to
    val transactionRepository: TransactionRepository = TransactionRepositoryImpl()

    // A function to create a window state and add it to _windows
    fun newWindow() {
        _windows.add(
            BankWindowState(
                application = this,
                exitDialog = exitDialog,
                exit = { exitWindow(it) }
            )
        )
    }

    // A function to close a window when it asks to be closed
    // If it is the last window it will prompt the user to save
    private suspend fun exitWindow(window: BankWindowState) {
        if (_windows.size == 1) {
            if (askToSave()) {
                _windows.remove(window)
            }
        } else {
            _windows.remove(window)
        }
    }

    // Function for sending notifications to the tray
    // TODO: Make notifications persist after app closes
    private fun sendNotification(notification: Notification) {
        tray.sendNotification(notification)
    }

    // Job for concurrently saving our csv data
    private var saveJob: Job? = null

    // Function to tell the job to save our data
    // If this function fails the user will be notified and the window won't close
    private suspend fun save(): Boolean {

        saveJob?.cancel()
        saveJob = launchSaving(customerRepository, accountRepository, transactionRepository)

        return try {
            saveJob?.join()
            sendNotification(BankApplicationNotification.SaveSuccess.format())
            true
        } catch (e: Exception) {
            e.printStackTrace()
            sendNotification(BankApplicationNotification.SaveError.format())
            false
        }
    }

    // Function to pop up the exit dialog and handle the user's selection
    // If the user selects yes, the app will attempt to save
    // If the save is successful, the app will close. If not, the app will notify the user and stay open
    private suspend fun askToSave(): Boolean {
        when (exitDialog.awaitResult()) {
            AlertDialogResult.Yes -> {
                if (save()) {
                    return true
                }
            }
            AlertDialogResult.No -> {
                return true
            }
            AlertDialogResult.Cancel -> return false
        }

        return false
    }
}

/**
 * This class represents the different notifications our app can send
 * This could be converted to an enum but sealed classes allow optional parameters for flexibility
 */
sealed class BankApplicationNotification {
    object SaveSuccess : BankApplicationNotification()
    object SaveError : BankApplicationNotification()
}

/**
 * This function is an extension function used to convert the sealed class objects to their corresponding notifications
 */
fun BankApplicationNotification.format() = when (this) {
    is BankApplicationNotification.SaveSuccess -> Notification(
        "File is saved", "", Notification.Type.Info
    )
    is BankApplicationNotification.SaveError -> Notification(
        "File isn't saved", "", Notification.Type.Error
    )
}

/**
 * This class holds the state for any dialog boxes we pop up to the user
 */
class DialogState<T> {
    private var onResult: CompletableDeferred<T>? by mutableStateOf(null)

    val isAwaiting get() = onResult != null

    suspend fun awaitResult(): T {
        onResult = CompletableDeferred()
        val result = onResult!!.await()
        onResult = null
        return result
    }

    fun onResult(result: T) = onResult!!.complete(result)
}

/**
 * Function to launch a coroutine that attempts to save our data to disk
 */
@OptIn(DelicateCoroutinesApi::class)
private fun launchSaving(customerRepository: CustomerRepository, accountRepository: AccountRepository, transactionRepository: TransactionRepository) = GlobalScope.launch {
    customerRepository.save()
    accountRepository.save()
    transactionRepository.save()
}