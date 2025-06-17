package com.umt.memingoapp.featureAr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

val FactColor = Color(0xFF21C17A)

data class FactItem(
    val factNumber: Int,
    val foreignLang: String,
    val foreignFlag: String,
    val foreignText: String,
    val englishFlag: String,
    val englishText: String
)

@Composable
fun FactList(
    modifier: Modifier = Modifier,
    facts: List<FactItem>
) {
    Column(modifier) {
        facts.forEachIndexed { idx, fact ->
            FactCard(fact = fact)
            if (idx < facts.lastIndex) Spacer(Modifier.height(14.dp))
        }
    }
}

@Composable
fun FactCard(
    fact: FactItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(FactColor.copy(alpha = 0.13f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = FactColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(7.dp))
                    Text(
                        "Fact ${fact.factNumber}",
                        color = FactColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        fact.foreignFlag,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        fact.foreignLang.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF646363),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Text(
                    fact.foreignText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFF232323),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        fact.englishFlag,
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
                    fact.englishText,
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
fun FactListPreview() {
    val facts = listOf(
        FactItem(
            factNumber = 4,
            foreignLang = "German",
            foreignFlag = "\uD83C\uDDE9\uD83C\uDDEA", // 🇩🇪
            foreignText = "Der Ringfinger ist traditionell der vierte Finger der Hand.",
            englishFlag = "\uD83C\uDDFA\uD83C\uDDF8", // 🇺🇸
            englishText = "The ring finger is traditionally the fourth finger of the hand."
        ),
        FactItem(
            factNumber = 5,
            foreignLang = "German",
            foreignFlag = "\uD83C\uDDE9\uD83C\uDDEA", // 🇩🇪
            foreignText = "Goldene Ringe sind in vielen Kulturen ein Symbol für Reichtum.",
            englishFlag = "\uD83C\uDDFA\uD83C\uDDF8", // 🇺🇸
            englishText = "Golden rings are a symbol of wealth in many cultures."
        )
    )
    Surface(color = Color(0xFFF7F7FB)) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            FactList(facts = facts)
        }
    }
}