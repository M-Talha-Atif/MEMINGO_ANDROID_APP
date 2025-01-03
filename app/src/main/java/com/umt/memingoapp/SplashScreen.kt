package com.umt.memingoapp

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umt.memingoapp.ui.theme.LogoColor
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.umt.memingoapp.navigation.Screen
import com.umt.memingoapp.models.listData
import kotlinx.coroutines.launch

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

@Preview( showBackground = true)
@Composable
fun TopHeaderPreview(){
    TopHeader()
}


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GetStartedScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        pageCount = { listData.size }
    )
    val (selectedPage, setSelectedPage) = remember {
        mutableStateOf(0)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            setSelectedPage(page)
        }
    }

    Scaffold {
        Column {
            HorizontalPager(
//                = listData.size,
                state = pagerState,
                modifier = Modifier.weight(0.6f)
            ) { page ->

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
 Image(painter = painterResource(listData[page].resId)
                    , contentDescription = "Image",
                    modifier = Modifier.size(350.dp))

                    Text(
                        listData[page].title,
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Box(modifier = Modifier.height(24.dp))
                    Text(
                        listData[page].desc,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                for (i in listData.indices) {
                    Box(
                        modifier = Modifier
                            .padding(end = if (i == listData.size - 1) 0.dp else 5.dp)
                            .width(if (i == selectedPage) 20.dp else 10.dp)
                            .height(10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (i == selectedPage) LogoColor else MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.1f
                                )
                            )
                    )
                }
            }

            // only show if not last page
            if (selectedPage != listData.size - 1) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    TextButton(
                        onClick = {
                            scope.launch {
                                /// animate to last page
                                pagerState.animateScrollToPage(listData.size - 1)
                            }
                        },
                        modifier = Modifier.height(56.dp)
                    ) {
                        Text(text = "Skip", color = LogoColor)
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                /// iterate to next screen
                                val nextPage = selectedPage + 1
                                pagerState.animateScrollToPage(nextPage)
                            }
                        },
                        modifier = Modifier.height(56.dp),
                        // to change button background color
                        colors = ButtonDefaults.buttonColors(containerColor = LogoColor)
                    ) {
                        Text(text = "Next", Modifier.background(LogoColor))
                    }
                }
            }

            /// show only in last page
            if (selectedPage == listData.size - 1) {
                Button(
                    onClick = {
                        navController.navigate(Screen.Login.route)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LogoColor),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Text(text = "Get Started", Modifier.background(LogoColor))
                }
            }
        }
    }
}







