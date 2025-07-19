package com.umt.memingoapp.presentation.shared_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
