package edu.missouriwestern.csc406team1.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.RenderVectorGroup
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter

/**
 * This object is used as a provider for our app resources
 */
val LocalAppResources = staticCompositionLocalOf<AppResources> {
    error("LocalBankAppResources isn't provided")
}

/**
 * This Composable remembers our app resources between recompositions
 * Used to prevent reloading resources over and over
 */
@Composable
fun rememberAppResources(): AppResources {
    val icon = rememberVectorPainter(Icons.Default.AccountBalance, tintColor = Color(0xFF2CA4E1))
    return remember { AppResources(icon) }
}

/**
 * A class for storing our resources, as of now there is only an icon
 */
class AppResources(val icon: VectorPainter)

/**
 * A composable to make our icon with some preset options
 */
@Composable
fun rememberVectorPainter(image: ImageVector, tintColor: Color) =
    rememberVectorPainter(
        defaultWidth = image.defaultWidth,
        defaultHeight = image.defaultHeight,
        viewportWidth = image.viewportWidth,
        viewportHeight = image.viewportHeight,
        name = image.name,
        tintColor = tintColor,
        tintBlendMode = image.tintBlendMode,
        autoMirror = false,
        content = { _, _ -> RenderVectorGroup(group = image.root) }
    )