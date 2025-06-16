package com.umt.memingoapp.featureAr

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umt.memingoapp.R
import com.umt.memingoapp.ui.theme.LogoColor


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewWordLearningCard() {
    WordLearningCard(
        wordName = "der Hund",
        wordDifficulty = "Beginner",
        article = "der",
        translation = "dog",
        onSoundClick = { /* Play sound */ },
        pronunciation = "hoont",
        category = "Animals",
        language = "German",
        onSaveLearningPack = { /* Save word */ },
        isSaved = false,
        onCompleteLearningPackClick = { /* Complete pack */ },
        sentencesCount = 3,
        tipsCount = 2,
        factsCount = 1,
        relatedCount = 4,
        modifier = Modifier
            .padding(14.dp)
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(24.dp))
            .background(LogoColor, shape = RoundedCornerShape(24.dp))
            .animateContentSize()
    )
}


@Composable
fun WordLearningCard(
    wordName: String,
    wordDifficulty: String,
    article: String,
    translation: String,
    onSoundClick: () -> Unit,
    pronunciation: String,
    category: String,
    language: String,
    onSaveLearningPack: () -> Unit,
    isSaved: Boolean,
    modifier: Modifier = Modifier,
    onCompleteLearningPackClick: () -> Unit = {},
    sentencesCount: Int = 5,
    tipsCount: Int = 5,
    factsCount: Int = 5,
    relatedCount: Int = 5
) {
    var expanded by remember { mutableStateOf(true) }
    Card(
        modifier = modifier
            .padding(14.dp)
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(24.dp))
            .background(LogoColor, shape = RoundedCornerShape(24.dp))
            .animateContentSize(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFD4E6FB),
                            Color(0xFFF9F9FB),
                        )
                    )
                )
                .padding(2.dp)
        ) {


            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Top Row: wordName + difficulty badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        wordName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 22.sp,
                            color = LogoColor
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    DifficultyBadge(wordDifficulty)
                }
                // Article, translation, sound
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "$article ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = LogoColor,
                            fontWeight = FontWeight.Normal,
                            fontSize = 22.sp
                        ),
                    )
                    Text(
                        translation,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = LogoColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                    )
                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(LogoColor.copy(alpha = 0.13f), shape = CircleShape)
                            .clickable { onSoundClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Play pronunciation",
                            tint = LogoColor,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
                // Pronunciation
                Text(
                    pronunciation,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = LogoColor.copy(alpha = 0.74f),
                        fontSize = 19.sp
                    ),
                    modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                )
                // Category and Language
                Row(
                    Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ChipIcon(
                        icon = Icons.Outlined.ListAlt,
                        label = category,
                        bgColor = Color(0xFFF0F3F8),
                        textColor = LogoColor
                    )
                    ChipIcon(
                        icon = null,
                        label = language,
                        bgColor = Color(0xFFF0F3F8),
                        textColor = LogoColor,
                        trailingIconRes = R.drawable.p1 // flag
                    )
                }
                // Save Learning Pack Button
                Button(
                    onClick = onSaveLearningPack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF36C97B),
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        if (isSaved) "Saved" else "Save Learning Pack",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                // Expandable Card: Complete Learning Package
                Spacer(Modifier.height(8.dp))
                CompleteLearningPackageCard(
//                expanded = expanded,
//                onClick = { expanded = !expanded },
                    sentencesCount = sentencesCount,
                    tipsCount = tipsCount,
                    factsCount = factsCount,
                    relatedCount = relatedCount
                )
            }
        }
    }

}



    @Composable
    fun DifficultyBadge(difficulty: String) {
        Box(
            modifier = Modifier
                .background(color = Color(0xFFDEF6E2), shape = RoundedCornerShape(16.dp))
                .padding(horizontal = 13.dp, vertical = 5.dp)
        ) {
            Text(
                text = difficulty,
                color = Color(0xFF1AC786),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }

    @Composable
    fun ChipIcon(
        icon: ImageVector?,
        label: String,
        bgColor: Color,
        textColor: Color,
        trailingIconRes: Int? = null
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(bgColor, shape = RoundedCornerShape(14.dp))
                .padding(horizontal = 11.dp, vertical = 6.dp)
        ) {
            if (icon != null)
                Icon(
                    icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(19.dp)
                )
            if (trailingIconRes != null)
                Icon(
                    painter = painterResource(trailingIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(19.dp),
                    tint = textColor
                )
            Spacer(Modifier.width(3.dp))
            Text(label, color = textColor, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }
    }



//@Composable
//fun CompleteLearningPackageCard(
//    expanded: Boolean,
//    onClick: () -> Unit,
//    sentencesCount: Int,
//    tipsCount: Int,
//    factsCount: Int,
//    relatedCount: Int
//) {
//    Card(
//        shape = RoundedCornerShape(16.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onClick() }
//            .animateContentSize(),
//        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
//    ) {
//        Column(
//            Modifier
//                .background(
//                    brush = Brush.verticalGradient(
//                        listOf(
//                            Color(0xFFE6F0FF),
//                            Color(0xFFFAFAFA)
//                        )
//                    ),
//                    shape = RoundedCornerShape(16.dp)
//                )
//                .padding(15.dp)
//        ) {
//            // Header row: Book icon + text
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(
//                    Icons.Default.MenuBook,
//                    contentDescription = null,
//                    tint = LogoColor,
//                    modifier = Modifier.size(28.dp)
//                )
//                Spacer(Modifier.width(7.dp))
//                Text(
//                    "Complete Learning Package",
//                    style = MaterialTheme.typography.titleMedium.copy(
//                        fontSize = 20.sp,
//                        color = LogoColor,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                )
//            }
//            if (expanded) {
//                Spacer(Modifier.height(10.dp))
//                // Two rows as requested
//                Row(
//                    Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    InfoItem(
//                        icon = Icons.Default.Star, text = "$sentencesCount Sentences", color = Color(0xFF379AE6)
//                    )
//                    InfoItem(
//                        icon = Icons.Outlined.Lightbulb, text = "$tipsCount Tips", color = Color(0xFFFFC107)
//                    )
//                }
//                Spacer(Modifier.height(8.dp))
//                Row(
//                    Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    InfoItem(
//                        icon = Icons.Default.Star, text = "$factsCount Facts", color = Color(0xFF36C97B)
//                    )
//                    InfoItem(
//                        icon = Icons.Outlined.Public, text = "$relatedCount Related", color = Color(0xFFB39DDB)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun InfoItem(icon: ImageVector, text: String, color: Color) {
//    Row(verticalAlignment = Alignment.CenterVertically) {
//        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
//        Spacer(Modifier.width(5.dp))
//        Text(text, color = color, fontWeight = FontWeight.Medium, fontSize = 15.sp)
//    }
//}

