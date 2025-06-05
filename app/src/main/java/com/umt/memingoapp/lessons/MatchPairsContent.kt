package com.umt.memingoapp.lessons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.models.responses.ActivityContent
import com.umt.memingoapp.models.responses.PairData
import com.umt.memingoapp.ui.theme.LogoColor


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MatchPairsContentPreview() {
    MaterialTheme {
        Surface {
            MatchPairsContent(
                content = ActivityContent.MatchPairs(
                    instruction = "Match the English words with their German translations",
                    pairs = listOf(
                        PairData("Hello", "Hallo"),
                        PairData("Goodbye", "Tschüss"),
                        PairData("Thank you", "Danke"),
                        PairData("Please", "Bitte")
                    )
                ),
                onCorrect = { println("Correct!") },
                onWrong = { println("Try again") }
            )
        }
    }
}
@Composable
fun MatchPairsContent(
    content: ActivityContent.MatchPairs,
    onCorrect: () -> Unit,
    onWrong: () -> Unit
) {
    val pairs = content.pairs
    val englishWords = remember(pairs) { pairs.map { it.english } }
    val germanWordsInit = remember(pairs) { pairs.map { it.german }.shuffled() }

    var germanWords by remember { mutableStateOf(germanWordsInit) }
    var selectedEnglishIdx by remember { mutableStateOf<Int?>(null) }
    var selectedGermanIdx by remember { mutableStateOf<Int?>(null) }
    var matchedPairs by remember { mutableStateOf<List<Pair<Int, Int>>>(emptyList()) }
    var feedbackState by remember { mutableStateOf<Pair<Boolean?, Boolean>>(null to false) } // (correct?, isChecking)
    val (isCorrect, isChecking) = feedbackState

    val feedbackColor by animateColorAsState(
        when (isCorrect) {
            true -> Color(0xFF4CAF50)
            false -> Color(0xFFD32F2F)
            else -> LogoColor
        },
        label = "feedback-color"
    )

    fun isMatched(engIdx: Int, gerIdx: Int) = matchedPairs.contains(engIdx to gerIdx)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = content.instruction,
            style = MaterialTheme.typography.titleLarge.copy(color = LogoColor)
        )

        Spacer(Modifier.height(18.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // English column
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                englishWords.forEachIndexed { engIdx, eng ->
                    val matched = matchedPairs.any { it.first == engIdx }
                    val selected = selectedEnglishIdx == engIdx && selectedGermanIdx == null
                    Surface(
                        color = when {
                            matched -> LogoColor.copy(alpha = 0.13f)
                            selected -> LogoColor.copy(alpha = 0.20f)
                            else -> Color.White
                        },
                        border = BorderStroke(2.dp, if (selected) LogoColor else LogoColor.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(14.dp),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 2.dp)
                            .clickable(enabled = !matched && isCorrect == null) {
                                if (selectedEnglishIdx == engIdx) {
                                    selectedEnglishIdx = null
                                } else {
                                    selectedEnglishIdx = engIdx
                                }
                            }
                    ) {
                        Text(
                            eng,
                            color = LogoColor,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            Spacer(Modifier.width(8.dp))
            // German column
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                germanWords.forEachIndexed { gerIdx, ger ->
                    val matched = matchedPairs.any { it.second == gerIdx }
                    val selected = selectedGermanIdx == gerIdx && selectedEnglishIdx != null
                    Surface(
                        color = when {
                            matched -> LogoColor.copy(alpha = 0.13f)
                            selected -> LogoColor.copy(alpha = 0.20f)
                            else -> Color.White
                        },
                        border = BorderStroke(2.dp, if (selected) LogoColor else LogoColor.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(14.dp),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 2.dp)
                            .clickable(enabled = !matched && selectedEnglishIdx != null && isCorrect == null) {
                                selectedGermanIdx = gerIdx
                                // Check for a match
                                if (selectedEnglishIdx != null) {
                                    val eng = englishWords[selectedEnglishIdx!!]
                                    val correct = pairs.any { it.english == eng && it.german == germanWords[gerIdx] }
                                    if (correct) {
                                        matchedPairs = matchedPairs + (selectedEnglishIdx!! to gerIdx)
                                    }
                                    selectedEnglishIdx = null
                                    selectedGermanIdx = null
                                }
                            }
                    ) {
                        Text(
                            ger,
                            color = LogoColor,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }

        // Reset and Check
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 26.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {
                    germanWords = germanWords.shuffled()
                    matchedPairs = emptyList()
                    selectedEnglishIdx = null
                    selectedGermanIdx = null
                    feedbackState = null to false
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(LogoColor.copy(alpha = 0.10f), shape = RoundedCornerShape(18.dp))
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = LogoColor)
            }
            Spacer(Modifier.width(24.dp))
            Button(
                onClick = {
                    val correct = matchedPairs.size == pairs.size &&
                            matchedPairs.all { (engIdx, gerIdx) ->
                                pairs[engIdx].german == germanWords[gerIdx]
                            }
                    feedbackState = correct to true
                },
                enabled = matchedPairs.size == pairs.size && isCorrect == null,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LogoColor)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Check", tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Check", color = Color.White)
            }
        }

        // Feedback
        AnimatedVisibility(visible = isCorrect != null, modifier = Modifier.padding(top = 20.dp)) {
            val correct = isCorrect == true
            Text(
                text = if (correct) "Correct!" else "Try again",
                color = feedbackColor,
                style = MaterialTheme.typography.titleLarge
            )
            LaunchedEffect(isChecking) {
                if (isChecking) {
                    kotlinx.coroutines.delay(1200)
                    feedbackState = null to false
                    matchedPairs = emptyList()
                    selectedEnglishIdx = null
                    selectedGermanIdx = null
                    if (correct) onCorrect() else onWrong()
                }
            }
        }
    }
}