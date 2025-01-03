package com.umt.memingoapp.uiComponents

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
            cursorColor = Color.Black,
            errorTextColor = Color.Black,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
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
    value: String, modifier: Modifier = Modifier,
    onChange: (String) -> Unit, label: String, placeholder: String,
    startIcon: @Composable () -> Unit
){


    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onChange,
        modifier =  modifier,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = TextFieldBackColor,  // Set custom background color
            cursorColor = Color.Black,
            errorTextColor = Color.Black,// Set cursor color
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,  // Customize the indicator color when focused
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)  // Unfocused indicator color
        ),

        label = { Text(label) },

//        textStyle = LocalTextStyle.current.copy(color = Color.White),
        leadingIcon = startIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down)}),
        placeholder = { Text(placeholder) },
        singleLine =  true,
        visualTransformation = VisualTransformation.None

    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    value: String, modifier: Modifier = Modifier,
    submit: () -> Unit,
    onChange: (String) -> Unit, label: String = "password", placeholder: String = "Enter your password"

){



    var isPasswordVisible by remember { mutableStateOf(false) }

    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Lock,
            contentDescription = "",
            tint = LogoColor
        )
    }

    val trailingIcon = @Composable {
        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon(
                if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "",
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
            cursorColor = Color.Black,
            errorTextColor = Color.Black,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        ),

        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),

        keyboardActions = KeyboardActions(
            onDone = {  //submit()
            }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )



}
