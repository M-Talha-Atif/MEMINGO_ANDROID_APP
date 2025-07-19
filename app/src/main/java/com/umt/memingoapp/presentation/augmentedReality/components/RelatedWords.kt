package com.umt.memingoapp.presentation.augmentedReality.components

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umt.memingoapp.domain.model.uiModels.RelatedWordItem
import java.util.Locale


// Helper function to map language name to Locale
fun getTTSLocale(language: String): Locale =
    when (language.lowercase()) {
        "german" -> Locale.GERMAN
        "english" -> Locale.ENGLISH
        "spanish" -> Locale("es")
        "italian" -> Locale.ITALIAN
        "french" -> Locale.FRENCH
        else -> Locale.getDefault()
    }

// Centralized TTS controller for Compose
@Composable
fun rememberSingleTextToSpeech(language: String): (String) -> Unit {
    val context = LocalContext.current
    val ttsLocale = remember(language) { getTTSLocale(language) }
    val tts = remember {
        TextToSpeech(context) { }
    }
    DisposableEffect(tts) {
        onDispose { tts.shutdown() }
    }
    // Set language whenever it changes
    LaunchedEffect(ttsLocale) {
        tts.language = ttsLocale
    }
    return remember(tts, ttsLocale) {
        { text: String ->
            tts.language = ttsLocale // Ensure correct language
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }
}


@Composable
fun RelatedWordsCard(
    modifier: Modifier = Modifier,
    language: String = "German",
    relatedWords: List<RelatedWordItem>
) {
    val playSound = rememberSingleTextToSpeech(language)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(7.dp, shape = MaterialTheme.shapes.large),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            Modifier
                .background(Color.White)
                .padding(18.dp)
        ) {
            // Title Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    Icons.Filled.Link,
                    contentDescription = null,
                    tint = Color(0xFF8852F9),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Related Words",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp,
                        color = Color(0xFF232323),
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            relatedWords.forEachIndexed { idx, wordItem ->
                RelatedWordListItem(
                    item = wordItem,
                    onSoundClick = { playSound(wordItem.word) }
                )
                if (idx < relatedWords.lastIndex) {
                    Divider(
                        color = Color(0xFFE7E7EC),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 7.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RelatedWordListItem(
    item: RelatedWordItem,
    onSoundClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = item.word,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFF267A82),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
            )
            Text(
                text = item.translation,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF7A7A7A),
                    fontSize = 16.sp
                )
            )
        }
        Surface(
            modifier = Modifier
                .size(36.dp)
                .padding(start = 5.dp),
            color = Color(0xFFE6F0FA),
            shape = CircleShape,
            shadowElevation = 0.dp,
            onClick = onSoundClick
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Filled.VolumeUp,
                    contentDescription = "Play sound",
                    tint = Color(0xFF267A82),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F7FB)
@Composable
fun RelatedWordsCardPreview() {
    val dummyWords = listOf(
        RelatedWordItem("Schmuck", "jewelry"),
        RelatedWordItem("Ehering", "wedding ring"),
        RelatedWordItem("Silber", "silver")
    )
    Surface(color = Color(0xFFF7F7FB)) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            RelatedWordsCard(
                relatedWords = dummyWords,
                language = "German"
            )
        }
    }
}