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
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.domain.model.response.ActivityContent
import com.umt.memingoapp.domain.model.response.PairData
import com.umt.memingoapp.presentation.theme.LogoColor

// --- PAIR HIGHLIGHT COLORS ---
val pairHighlightColors = listOf(
    Color(0xFF7BB6FF),
    Color(0xFFFFB870),
    Color(0xFF6EE7B7),
    Color(0xFFFFB7CE),
    Color(0xFFFFF07F),
    Color(0xFFB5A0FF),
    Color(0xFFFFA6A0),
    Color(0xFF80FFD8)
)

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
    val germanWordsInit = remember(pairs) {
        pairs.map { it.german ?: it.spanish ?: it.italian ?: "" }.shuffled()
    }

    var germanWords by remember { mutableStateOf(germanWordsInit) }
    var selectedEnglishIdx by remember { mutableStateOf<Int?>(null) }
    var selectedGermanIdx by remember { mutableStateOf<Int?>(null) }

    // Each matched pair is (englishIdx, germanIdx) to colorIndex for highlight
    var matchedPairs by remember { mutableStateOf<List<Triple<Int, Int, Int>>>(emptyList()) }
    var feedbackState by remember { mutableStateOf<Pair<Boolean?, Boolean>>(null to false) }
    val (isCorrect, isChecking) = feedbackState

    // Find colorIndex for a given english or german index if matched, else null
    fun getPairColorIndex(englishIdx: Int? = null, germanIdx: Int? = null): Int? {
        return matchedPairs.find {
            (englishIdx != null && it.first == englishIdx) ||
                    (germanIdx != null && it.second == germanIdx)
        }?.third
    }

    // Modular: Single column of selectable words
    @Composable
    fun WordColumn(
        words: List<String>,
        selectedIdx: Int?,
        matchedIdxs: Set<Int>,
        matchedColorGetter: (Int) -> Int?, // returns colorIndex for matched idx
        otherSelectionActive: Boolean,
        onSelect: (Int) -> Unit,
        isEnglish: Boolean,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = if (isEnglish) Alignment.End else Alignment.Start
        ) {
            words.forEachIndexed { idx, word ->
                val matchedColorIdx = matchedColorGetter(idx)
                val selected = selectedIdx == idx
                val isMatched = matchedIdxs.contains(idx)
                val highlightColor = matchedColorIdx?.let { pairHighlightColors[it % pairHighlightColors.size] }
                val baseHighlightColor = LogoColor
                val highlightColorFinal = highlightColor ?: baseHighlightColor

                val bgColor = when {
                    isMatched -> highlightColorFinal
                    selected -> baseHighlightColor // no alpha
                    else -> Color.White
                }

                val textColor = when {
                    isMatched || selected -> Color.White
                    else -> baseHighlightColor
                }

                val animatedBg by animateColorAsState(bgColor, label = "bg-color")
                val animatedTextColor by animateColorAsState(textColor, label = "text-color")


                Surface(
                    color = animatedBg,
                    border = BorderStroke(2.dp, if (selected) LogoColor else LogoColor.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(14.dp),
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        // marginStart/marginEnd equivalent:
                        .padding(horizontal = 8.dp) // <-- horizontal margin for each box
                        .clickable(
                            enabled = !isMatched && (if (isEnglish) selectedGermanIdx == null else selectedEnglishIdx != null) && isCorrect == null
                        ) { onSelect(idx) }
                ) {
                    Text(
                        word,
                        color = animatedTextColor,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }

    // Main UI
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
            // English
            WordColumn(
                words = englishWords,
                selectedIdx = selectedEnglishIdx,
                matchedIdxs = matchedPairs.map { it.first }.toSet(),
                matchedColorGetter = { engIdx -> getPairColorIndex(englishIdx = engIdx) },
                otherSelectionActive = selectedGermanIdx != null,
                onSelect = { engIdx -> selectedEnglishIdx = if (selectedEnglishIdx == engIdx) null else engIdx },
                isEnglish = true ,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            // German
            WordColumn(
                words = germanWords,
                selectedIdx = selectedGermanIdx,
                matchedIdxs = matchedPairs.map { it.second }.toSet(),
                matchedColorGetter = { gerIdx -> getPairColorIndex(germanIdx = gerIdx) },
                otherSelectionActive = selectedEnglishIdx != null,
                onSelect = { gerIdx ->
                    selectedGermanIdx = gerIdx
                    // Only allow selection if English side is selected
                    if (selectedEnglishIdx != null) {
                        val eng = englishWords[selectedEnglishIdx!!]
                        val correct = pairs.any {
                            it.english == eng && (
                                    (it.german != null && it.german == germanWords[gerIdx])
                                            || (it.spanish != null && it.spanish == germanWords[gerIdx])
                                            || (it.italian != null && it.italian == germanWords[gerIdx])
                                    )
                        }
                        val colorIdx = matchedPairs.size % pairHighlightColors.size
                        matchedPairs = matchedPairs + Triple(selectedEnglishIdx!!, gerIdx, colorIdx)

//                        if (correct) {
//                            // Assign next color index for new pair
//                            val colorIdx = matchedPairs.size % pairHighlightColors.size
//                            matchedPairs = matchedPairs + Triple(selectedEnglishIdx!!, gerIdx, colorIdx)
//                        }

                        // Reset selection for next match attempt
                        selectedEnglishIdx = null
                        selectedGermanIdx = null
                    }
                },
                isEnglish = false,
                modifier = Modifier.weight(1f)
            )
        }

        // Reset and Check controls
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
                            matchedPairs.all { (engIdx, tarIdx, _) ->
                                val pair = pairs[engIdx]
                                val target = germanWords[tarIdx]
                                (pair.german == target) || (pair.spanish == target) || (pair.italian == target)
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
            val feedbackColor by animateColorAsState(
                when (isCorrect) {
                    true -> Color(0xFF4CAF50)
                    false -> Color(0xFFD32F2F)
                    else -> LogoColor
                },
                label = "feedback-color"
            )
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

/*
---------------------
Technical Flow Explained:
---------------------
1. The screen displays two columns: English words (left) and German words (right, shuffled).
2. User first selects an English word (highlighted). Only one can be selected at a time.
3. Then, user selects a German word. If it's a correct match, both are locked in a unique color and cannot be selected again.
   - Each matched pair gets its own color from a predefined palette for easy differentiation.
   - Only unmatched words are selectable.
4. User can't select two words from the same column before matching, and can't select already paired words.
5. The "Check" button validates all matches and shows feedback ("Correct!" or "Try again").
6. The "Reset" button shuffles and clears all selections/matches.
7. The UI is broken into modular components:
   - MatchPairsContent: Main logic, state, high-level layout.
   - WordColumn: Reusable column for either language, handles selection, coloring, and click logic.
   - Color logic is handled by checking the pair's color index in matchedPairs.
8. Margin is applied by Modifier.padding(horizontal = 8.dp) on each box, which is Compose's equivalent of `marginStart`/`marginEnd` in XML.
*/