package com.umt.memingoapp.presentation.augmentedReality.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.umt.memingoapp.domain.model.uiModels.UsageTip
import com.umt.memingoapp.ui.theme.LogoColor


@Composable
fun UsageTipsList(
    modifier: Modifier = Modifier,
    tips: List<UsageTip>
) {
    Column(modifier) {
        // Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp, top = 2.dp, start=16.dp)
        ) {
            Icon(
                Icons.Filled.Lightbulb,
                contentDescription = null,
                tint = LogoColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Usage Tips",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp,
                    color = Color(0xFF232323),
                    fontWeight = FontWeight.SemiBold
                )

            )
        }


        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            itemsIndexed(tips) { idx, tip ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { visible = true }
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn()
                ) {
                    UsageTipCard(
                        tip = tip,
                        // marginStart/marginEnd in XML = padding(horizontal) in Compose
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp, // <-- horizontal margin for each card
                                vertical = 4.dp
                            )
                    )
                }
                if (idx < tips.lastIndex) {
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun UsageTipCard(tip: UsageTip, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Column(
                Modifier.padding(start = 12.dp)
            ) {
                // Tip number and icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Lightbulb,
                        contentDescription = null,
                        tint = LogoColor,
                        modifier = Modifier.size(21.dp)
                    )
                    Spacer(Modifier.width(7.dp))
                    Text(
                        "Tip ${tip.tipNumber}",
                        color = LogoColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(Modifier.height(10.dp))
                // Foreign lang
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        tip.foreignFlag,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        tip.foreignLang.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF646363),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Text(
                    tip.foreignText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFF232323),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                // English lang
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        tip.englishFlag,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        "ENGLISH",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF646363),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Text(
                    tip.englishText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFF7A7A7A),
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F7FB)
@Composable
fun UsageTipsPreview() {
    val tips = listOf(
        UsageTip(
            tipNumber = 1,
            foreignLang = "German",
            foreignFlag = "\uD83C\uDDE9\uD83C\uDDEA", // 🇩🇪
            foreignText = "Ringe werden oft als Schmuck getragen.",
            englishFlag = "\uD83C\uDDFA\uD83C\uDDF8", // 🇺🇸
            englishText = "Rings are often worn as jewelry."
        ),
        UsageTip(
            tipNumber = 2,
            foreignLang = "German",
            foreignFlag = "\uD83C\uDDE9\uD83C\uDDEA", // 🇩🇪
            foreignText = "Ein Ring kann ein Symbol für Ehe oder Freundschaft sein.",
            englishFlag = "\uD83C\uDDFA\uD83C\uDDF8", // 🇺🇸
            englishText = "A ring can be a symbol of marriage or friendship."
        )
    )
    Surface(color = Color(0xFFF7F7FB)) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            UsageTipsList(tips = tips)
        }
    }
}