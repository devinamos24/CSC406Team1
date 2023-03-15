package edu.missouriwestern.csc406team1.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import edu.missouriwestern.csc406team1.ArrayListFlow

/**
 * This is an extension function for collecting state from our modified arraylist
 */
@Composable
fun <T> ArrayListFlow<T>.collectAsState(): State<List<T>> {
    return flow.collectAsState().apply { toList() }
}