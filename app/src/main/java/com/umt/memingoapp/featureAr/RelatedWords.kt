package com.umt.memingoapp.featureAr

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class RelatedWordItem(
    val foreignWord: String,
    val englishWord: String,
    val onSoundClick: () -> Unit = {}
)

@Composable
fun RelatedWordsCard(
    modifier: Modifier = Modifier,
    language: String = "German",
    relatedWords: List<RelatedWordItem>
) {
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
                        fontWeight = FontWeight.SemiBold,
                        // Replace with your handwriting font if needed
                    )
                )
            }
            relatedWords.forEachIndexed { idx, wordItem ->
                RelatedWordListItem(wordItem)
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
    item: RelatedWordItem
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = item.foreignWord,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFF267A82),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    // Replace with handwriting font if needed
                )
            )
            Text(
                text = item.englishWord,
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
            onClick = item.onSoundClick
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
                relatedWords = dummyWords
            )
        }
    }
}