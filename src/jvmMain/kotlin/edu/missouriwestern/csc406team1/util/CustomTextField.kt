package edu.missouriwestern.csc406team1.util

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    inputWrapper: InputWrapper,
    label: String,
    keyboardOptions: KeyboardOptions = remember {
        KeyboardOptions.Default
    },
    visualTransformation: VisualTransformation = remember {
        VisualTransformation.None
    },
    onValueChange: OnValueChange,
//    onImeKeyAction: OnImeKeyAction
    shape: Shape = TextFieldDefaults.filledShape

) {
    Column {
        TextField(
            modifier = modifier,
            value = inputWrapper.value,
            onValueChange = {
                onValueChange(it)
            },
            label = { Text(label) },
            isError = inputWrapper.errorMessage != null,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            shape = shape,
//            keyboardActions = remember {
//                KeyboardActions(onAny = { onImeKeyAction() })
//            },
        )
//        if (inputWrapper.errorMessage != null) {
//            Text(
//                text = inputWrapper.errorMessage,
//                color = MaterialTheme.colors.error,
//                style = MaterialTheme.typography.caption,
//                modifier = Modifier.padding(start = 16.dp)
//            )
//        }
    }
}