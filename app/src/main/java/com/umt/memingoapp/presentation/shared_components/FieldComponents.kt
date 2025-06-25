package com.umt.memingoapp.presentation.shared_components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.umt.memingoapp.ui.theme.LogoColor
import com.umt.memingoapp.ui.theme.TextFieldBackColor



val LogoColor =  LogoColor         // replace with your brand's primary color
val TextFieldBackColor = Color(0xFFF2F2F2)    // background of text fields
val TextColor = Color.Black                  // or white depending on your theme
val PlaceholderColor = Color.Gray            // or LogoColor.copy(alpha = 0.6f)
val LabelColor = LogoColor                   // or whatever matches your style


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneField(
    value: String,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit,
    label: String,
    placeholder: String,
    startIcon: @Composable () -> Unit
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = {
            // Allow only numbers and optional "+" for international numbers
            if (it.all { char -> char.isDigit() || char == '+' }) {
                onChange(it)
            }
        },
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = TextFieldBackColor,
            cursorColor = LogoColor,
            focusedLabelColor = LabelColor,
            unfocusedLabelColor = LabelColor.copy(alpha = 0.8f),
            focusedIndicatorColor = LogoColor,
            unfocusedIndicatorColor = LogoColor.copy(alpha = 0.5f),
            disabledIndicatorColor = LogoColor.copy(alpha = 0.3f),
            errorIndicatorColor = Color.Red,
            errorLabelColor = Color.Red,
            errorCursorColor = Color.Red
        ),
        label = { Text(label) },
        leadingIcon = startIcon,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        placeholder = { Text(placeholder) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleField(
    value: String,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit,
    label: String,
    placeholder: String,
    startIcon: @Composable () -> Unit
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = TextFieldBackColor,
            cursorColor = LogoColor,
            focusedLabelColor = LabelColor,
            unfocusedLabelColor = LabelColor.copy(alpha = 0.8f),
            focusedIndicatorColor = LogoColor,
            unfocusedIndicatorColor = LogoColor.copy(alpha = 0.5f),
            disabledIndicatorColor = LogoColor.copy(alpha = 0.3f),
            errorIndicatorColor = Color.Red,
            errorLabelColor = Color.Red,
            errorCursorColor = Color.Red
        ),
        label = { Text(label) },
        leadingIcon = startIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(placeholder) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    value: String,
    modifier: Modifier = Modifier,
    submit: () -> Unit,
    onChange: (String) -> Unit,
    label: String = "Password",
    placeholder: String = "Enter your password"
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    val leadingIcon = @Composable {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = LogoColor
        )
    }

    val trailingIcon = @Composable {
        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon(
                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = null,
                tint = LogoColor
            )
        }
    }

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = TextFieldBackColor,
            cursorColor = LogoColor,
            focusedLabelColor = LabelColor,
            unfocusedLabelColor = LabelColor.copy(alpha = 0.8f),
            focusedIndicatorColor = LogoColor,
            unfocusedIndicatorColor = LogoColor.copy(alpha = 0.5f),
            disabledIndicatorColor = LogoColor.copy(alpha = 0.3f),
            errorIndicatorColor = Color.Red,
            errorLabelColor = Color.Red,
            errorCursorColor = Color.Red
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(onDone = { submit() }),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

