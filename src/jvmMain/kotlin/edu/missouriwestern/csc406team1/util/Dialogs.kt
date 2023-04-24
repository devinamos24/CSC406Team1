package edu.missouriwestern.csc406team1.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.window.WindowScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import javax.swing.JOptionPane

/**
 * A Composable for displaying a yes, no, or cancel dialog
 * This is multipurpose, but as of now it is only used for saving when exiting
 */
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun WindowScope.YesNoCancelDialog(
    title: String,
    message: String,
    onResult: (result: AlertDialogResult) -> Unit
) {
    DisposableEffect(Unit) {
        val job = GlobalScope.launch(Dispatchers.Swing) {
            val resultInt = JOptionPane.showConfirmDialog(
                window, message, title, JOptionPane.YES_NO_CANCEL_OPTION
            )
            val result = when (resultInt) {
                JOptionPane.YES_OPTION -> AlertDialogResult.Yes
                JOptionPane.NO_OPTION -> AlertDialogResult.No
                else -> AlertDialogResult.Cancel
            }
            onResult(result)
        }

        onDispose {
            job.cancel()
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun WindowScope.OkDialog(
    title: String,
    message: String,
    onResult: () -> Unit
) {
    DisposableEffect(Unit) {
        val job = GlobalScope.launch(Dispatchers.Swing) {
            JOptionPane.showConfirmDialog(window, message, title, JOptionPane.OK_OPTION)
            onResult()
        }
        onDispose {
            job.cancel()
        }
    }
}

/**
 * An enum for representing our dialog options
 */
enum class AlertDialogResult {
    Yes, No, Cancel
}