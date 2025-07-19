package com.umt.memingoapp.presentation.splash.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.umt.memingoapp.R
import com.umt.memingoapp.domain.model.uiModels.listData
import com.umt.memingoapp.presentation.navigation.routes.Screen
import kotlinx.coroutines.launch
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.rememberNavController


@Composable
fun TopHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.icon_1),
            contentDescription = "HeaderImage",
            modifier = Modifier.size(40.dp)
        )
        Text(
            "Memingo",
            modifier = Modifier.padding(start = 10.dp),
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopHeaderPreview() {
    MaterialTheme {
        TopHeader()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GetStartedScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        pageCount = { listData.size }
    )
    val selectedPage by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            scope.launch {
                pagerState.animateScrollToPage(page)
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(0.6f)
            ) { page ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Image(
                        painter = painterResource(listData[page].resId),
                        contentDescription = "Image",
                        modifier = Modifier.size(350.dp)
                    )
                    Text(
                        text = listData[page].title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = listData[page].desc,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Page indicators
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                repeat(listData.size) { i ->
                    Box(
                        modifier = Modifier
                            .padding(end = if (i == listData.size - 1) 0.dp else 8.dp)
                            .size(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(
                                if (i == pagerState.currentPage)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                    )
                }
            }

            // Navigation buttons
            if (pagerState.currentPage != listData.size - 1) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    TextButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(listData.size - 1)
                            }
                        }
                    ) {
                        Text(
                            text = "Skip",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(text = "Next")
                    }
                }
            } else {
                Button(
                    onClick = { navController.navigate(Screen.Login.route) },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Get Started")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GetStartedScreenPreview() {
    MaterialTheme {
        GetStartedScreen(rememberNavController())
    }
}