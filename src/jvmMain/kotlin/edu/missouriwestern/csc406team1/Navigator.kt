import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigationSource
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.essenty.parcelable.Parcelable

/**
 * This keeps track of the local context of the window, mainly only needed for android but doesn't hurt
 */
val LocalComponentContext: ProvidableCompositionLocal<ComponentContext> =
    staticCompositionLocalOf { error("Root component context was not provided") }

/**
 * This provides the context from the LocalComponentContext to the composable
 * This tells android the context of the view they are in
 */
@Composable
fun ProvideComponentContext(componentContext: ComponentContext, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalComponentContext provides componentContext, content = content)
}

/**
 * This is a composable that is responsible for creating a ChildStack from our inputs
 */
@Composable
inline fun <reified C : Parcelable> ChildStack(
    source: StackNavigationSource<C>,
    noinline initialStack: () -> List<C>,
    modifier: Modifier = Modifier,
    key: String = "DefaultChildStack",
    handleBackButton: Boolean = false,
    animation: StackAnimation<C, ComponentContext>? = null,
    noinline content: @Composable (C) -> Unit,
) {
    ChildStack(
        stack = rememberChildStack(
            source = source,
            initialStack = initialStack,
            key = key,
            handleBackButton = handleBackButton,
        ),
        modifier = modifier,
        animation = animation,
        content = content,
    )
}

/**
 * This composable is responsible for keeping track of and rendering the stack
 */
@Composable
fun <C : Any> ChildStack(
    stack: State<ChildStack<C, ComponentContext>>,
    modifier: Modifier = Modifier,
    animation: StackAnimation<C, ComponentContext>? = null,
    content: @Composable (C) -> Unit,
) {
    Children(
        stack = stack.value,
        modifier = modifier,
        animation = animation,
    ) { child ->
        ProvideComponentContext(child.instance) {
            content(child.configuration)
        }
    }
}

/**
 * This composable is simply responsible for keeping the stack alive between compositions.
 * Compositions are when the screen re-renders, wouldn't want our app to restart every composition would we?
 */
@Composable
inline fun <reified C : Parcelable> rememberChildStack(
    source: StackNavigationSource<C>,
    noinline initialStack: () -> List<C>,
    key: String = "DefaultChildStack",
    handleBackButton: Boolean = false,
): State<ChildStack<C, ComponentContext>> {
    val componentContext = LocalComponentContext.current

    return remember {
        componentContext.childStack(
            source = source,
            initialStack = initialStack,
            key = key,
            handleBackButton = handleBackButton,
            childFactory = { _, childComponentContext -> childComponentContext },
        )
    }.subscribeAsState()
}
