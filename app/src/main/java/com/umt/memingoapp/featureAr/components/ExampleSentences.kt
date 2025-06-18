package com.umt.memingoapp.featureAr.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.umt.memingoapp.featureAr.models.ExampleSentence
import com.umt.memingoapp.ui.theme.LogoColor



@Composable
fun ExampleSentencesList(
    modifier: Modifier = Modifier,
    sentences: List<ExampleSentence>
) {
    Column(modifier) {
        // Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp, top = 2.dp, start=16.dp)
        ) {
            Icon(
                Icons.Filled.ChatBubbleOutline,
                contentDescription = null,
                tint = LogoColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Example Sentences",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp,
                    color = LogoColor,
                    fontWeight = FontWeight.SemiBold
                ),
            )
        }

        // Use LazyColumn for scrollability and animation
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            itemsIndexed(sentences) { idx, s ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { visible = true }
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn()
                ) {
                    ExampleSentenceCard(
                        sentence = s,
                        // marginStart/marginEnd in XML = padding(horizontal) in Compose
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp, // <-- horizontal margin for each card
                                vertical = 4.dp
                            )
                    )
                }
                if (idx < sentences.lastIndex) {
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun ExampleSentenceCard(sentence: ExampleSentence, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp)
    ) {
        Box(
            Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    // Number badge
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(LogoColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "${sentence.number}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    // Sound button
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F7FD))
                            .padding(0.dp)
                            .align(Alignment.Top)
                    ) {
                        Icon(
                            Icons.Filled.VolumeUp,
                            contentDescription = "Play sound",
                            tint = LogoColor,
                            modifier = Modifier
                                .size(22.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                // Foreign lang
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        sentence.foreignFlag,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(
                        sentence.foreignLang.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF646363),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Text(
                    sentence.foreignText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFF232323),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                // English lang
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        sentence.englishFlag,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 5.dp)
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
                    sentence.englishText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFF7A7A7A),
                        fontSize = 17.sp
                    )
                )
                Text(
                    sentence.ipa,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFF7A7A7A),
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F7FB)
@Composable
fun ExampleSentencesPreview() {
    val sentences = listOf(
        ExampleSentence(
            number = 1,
            foreignLang = "German",
            foreignFlag = "\uD83C\uDDE9\uD83C\uDDEA", // 🇩🇪
            foreignText = "Ich trage einen Ring am Finger.",
            englishFlag = "\uD83C\uDDFA\uD83C\uDDF8", // 🇺🇸
            englishText = "I wear a ring on my finger.",
            ipa = "/ɪç ˈtʁaːɡə ˈaɪ̯nən ʁɪŋ am ˈfɪŋɐ/"
        ),
        ExampleSentence(
            number = 2,
            foreignLang = "German",
            foreignFlag = "\uD83C\uDDE9\uD83C\uDDEA", // 🇩🇪
            foreignText = "Der Ring ist aus Silber.",
            englishFlag = "\uD83C\uDDFA\uD83C\uDDF8", // 🇺🇸
            englishText = "The ring is made of silver.",
            ipa = "/deːɐ̯ ʁɪŋ ɪst aʊs ˈzɪlbɐ/"
        )
    )
    Surface(color = Color(0xFFF7F7FB)) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            ExampleSentencesList(sentences = sentences)
        }
    }
}