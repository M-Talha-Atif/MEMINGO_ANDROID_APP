package com.umt.memingoapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umt.memingoapp.ui.theme.LogoColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.umt.memingoapp.ui.theme.TextFieldBackColor

@Composable
fun TopHeader(){
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically)
    {
        Image(painter = painterResource(R.drawable.icon_1),
            contentDescription = "HeaderImage",
            modifier = Modifier.size(40.dp)
        )
        Text("Memingo",
            modifier = Modifier.padding(start = 10.dp),
            style = TextStyle(color = LogoColor, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        )

    }
}
@Composable
fun Separator(){
    Row( modifier = Modifier.fillMaxWidth().padding(start = 4.dp,end = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
        ){
        HorizontalDivider(thickness = 1.dp,modifier = Modifier.weight(1f))
        Text(
            text = "or",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodySmall
        )
        HorizontalDivider(thickness = 1.dp,modifier = Modifier.weight(1f))

    }

}

//@Preview( showBackground = true, showSystemUi = true)
//@Composable
//fun SeparatorPreview(){
//    Separator()
//}


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


@Preview( showBackground = true)
@Composable
fun TopHeaderPreview(){
    TopHeader()
}