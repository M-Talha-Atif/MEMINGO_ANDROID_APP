package com.umt.memingoapp.presentation.augmentedReality.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.umt.memingoapp.presentation.augmentedReality.components.ARLearningTopBar
import com.umt.memingoapp.presentation.augmentedReality.components.ExampleSentencesList
import com.umt.memingoapp.presentation.augmentedReality.components.FactList
import com.umt.memingoapp.presentation.augmentedReality.components.RelatedWordsCard
import com.umt.memingoapp.presentation.augmentedReality.components.TabShifterBar
import com.umt.memingoapp.presentation.augmentedReality.components.TabType
import com.umt.memingoapp.presentation.augmentedReality.components.UsageTipsList
import com.umt.memingoapp.presentation.augmentedReality.components.WordLearningCard
import com.umt.memingoapp.presentation.augmentedReality.viewmodel.ARLearningViewModelFactory
import com.umt.memingoapp.domain.model.uiModels.ExampleSentence
import com.umt.memingoapp.domain.model.uiModels.FactItem
import com.umt.memingoapp.domain.model.uiModels.UsageTip
import com.umt.memingoapp.domain.model.uiModels.WordLearningContent
import com.umt.memingoapp.presentation.augmentedReality.viewmodel.ARLearningUiState
import com.umt.memingoapp.presentation.augmentedReality.viewmodel.ARLearningViewModel
import com.umt.memingoapp.presentation.shared_components.LoadingAnimation

private const val TAG = "ARLearningScreen"

@Composable
fun ARLearningScreen(
    moduleTitle: String,
    courseId: String,
    language: String,
    wordName: String,
    navController: NavController,
    onClose: () -> Unit,
    onScan: () -> Unit,
    viewModel: ARLearningViewModel = viewModel(factory = ARLearningViewModelFactory())
) {
    Log.d(TAG, "Composing ARLearningScreen for word: $wordName in $language")
    Log.d(TAG, "Module title: $moduleTitle, Course ID: $courseId")

    val uiState by viewModel.uiState.collectAsState()
    Log.d(TAG, "Current UI state: $uiState")

    // Add this DisposableEffect to handle cleanup
    DisposableEffect(Unit) {
        onDispose {
            Log.d("ARLearningScreen", "Composable being disposed")
            viewModel.cleanupResources()
        }
    }

    LaunchedEffect(wordName) {
        Log.d(TAG, "Launching content generation for word: $wordName")
        viewModel.generateContent(wordName, language)
    }

    when (val state = uiState) {
        is ARLearningUiState.Loading -> {
            Log.d(TAG, "Showing loading state")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingAnimation()
                Text("Loading content for $wordName...")
            }
        }

        is ARLearningUiState.Error -> {
            Log.e(TAG, "Error state: ${state.message}")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Error, contentDescription = "Error", tint = Color.Red)
                    Text("Error: ${state.message}")
                }
            }
        }

        is ARLearningUiState.Success -> {
            Log.d(TAG, "Showing success state with content")
            val content = state.content
            var selectedTab by remember { mutableStateOf(TabType.Overview) }
            Log.d(TAG, "Initial tab selected: $selectedTab")

            Surface(color = Color(0xFFF6F7FA)) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF6F7FA))
                ) {
                    Log.d(TAG, "Composing ARLearningTopBar")
                    ARLearningTopBar(
                        moduleTitle = moduleTitle,
                        language = language,
                        onClose = {
                            Log.d(TAG, "Close button clicked")
                            onClose()
                        },
                        onScan = {
                            Log.d(TAG, "Scan button clicked")
                            onScan()
                        }
                    )

                    Log.d(TAG, "Composing TabShifterBar")
                    TabShifterBar(
                        selectedTab = selectedTab,
                        onTabSelected = {
                            Log.d(TAG, "Tab changed to: $it")
                            selectedTab = it
                        },
                        modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                    )

                    Log.d(TAG, "Composing content for tab: $selectedTab")
                    when (selectedTab) {
                        TabType.Overview -> {
                            Log.d(TAG, "Showing Overview tab")
                            OverviewTab(content)
                        }
                        TabType.Sentences -> {
                            Log.d(TAG, "Showing Sentences tab with ${content.sentences.size} sentences")
                            SentencesTab(content.sentences)
                        }
                        TabType.Tips -> {
                            Log.d(TAG, "Showing Tips tab with ${content.tips.size} tips")
                            TipsTab(content.tips)
                        }
                        TabType.Facts -> {
                            Log.d(TAG, "Showing Facts tab with ${content.facts.size} facts")
                            FactsTab(content.facts)
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun OverviewTab(content: WordLearningContent) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {
        WordLearningCard(
            wordName = content.word,
            wordDifficulty = content.difficulty,
            article = content.article ?: "",
            translation = content.translation,
            pronunciation = content.pronunciation,
            category = content.category,
            language = content.language,
            sentencesCount = content.sentences.size,
            tipsCount = content.tips.size,
            factsCount = content.facts.size,
            relatedCount = content.relatedWords.size,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
            onSoundClick = {},
            onSaveLearningPack = {},
            isSaved = false,
            onCompleteLearningPackClick = {}
        )

        RelatedWordsCard(
            relatedWords = content.relatedWords,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun SentencesTab(sentences: List<ExampleSentence>) {
    ExampleSentencesList(sentences = sentences)
}

@Composable
private fun TipsTab(tips: List<UsageTip>) {
    UsageTipsList(tips = tips)
}

@Composable
private fun FactsTab(facts: List<FactItem>) {
    FactList(facts = facts)
}

@Composable
private fun ExampleSentenceItem(
    number: Int,
    foreignLang: String,
    foreignFlag: String,
    foreignText: String,
    englishFlag: String,
    englishText: String,
    ipa: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color(0xFF256470), CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number.toString(),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Example Sentence",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFF256470)
                )
            }

            LanguageTextRow(
                flag = foreignFlag,
                text = foreignText,
                language = foreignLang,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            LanguageTextRow(
                flag = englishFlag,
                text = englishText,
                language = "English",
                textColor = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Pronunciation: $ipa",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
private fun LanguageTextRow(
    flag: String,
    text: String,
    language: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black
) {
    Row(modifier = modifier) {
        Text(
            text = flag,
            modifier = Modifier.padding(end = 8.dp),
            fontSize = 20.sp
        )
        Column {
            Text(
                text = language,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
    }
}

//@Preview(showBackground = true, backgroundColor = 0xFFF6F7FA)
//@Composable
//fun ARLearningScreenPreview() {
//    // Dummy data
//    val relatedWords = listOf(
//        RelatedWordItem("Schmuck", "jewelry"),
//        RelatedWordItem("Ehering", "wedding ring"),
//        RelatedWordItem("Silber", "silver")
//    )
//    val tips = listOf(
//        UsageTip(
//            tipNumber = 1,
//            foreignLang = "German",
//            foreignFlag = "\uD83C\uDDE9\uD83C\uDDEA", // 🇩🇪
//            foreignText = "Ringe werden oft als Schmuck getragen.",
//            englishFlag = "\uD83C\uDDFA\uD83C\uDDF8", // 🇺🇸
//            englishText = "Rings are often worn as jewelry."
//        ),
//        UsageTip(
//            tipNumber = 2,
//            foreignLang = "German",
//            foreignFlag = "\uD83C\uDDE9\uD83C\uDDEA", // 🇩🇪
//            foreignText = "Ein Ring kann ein Symbol für Ehe oder Freundschaft sein.",
//            englishFlag = "\uD83C\uDDFA\uD83C\uDDF8", // 🇺🇸
//            englishText = "A ring can be a symbol of marriage or friendship."
//        ),
//        UsageTip(
//            tipNumber = 3,
//            foreignLang = "German",
//            foreignFlag = "\uD83C\uDDE9\uD83C\uDDEA", // 🇩🇪
//            foreignText = "Ein Ring kann ein Symbol für Ehe oder Freundschaft sein.",
//            englishFlag = "\uD83C\uDDFA\uD83C\uDDF8", // 🇺🇸
//            englishText = "A ring can be a symbol of marriage or friendship."
//        )
//    )
//    val facts = listOf(
//        FactItem(
//            factNumber = 4,
//            foreignLang = "German",
//            foreignFlag = "\uD83C\uDDE9\uD83C\uDDEA", // 🇩🇪
//            foreignText = "Der Ringfinger ist traditionell der vierte Finger der Hand.",
//            englishFlag = "\uD83C\uDDFA\uD83C\uDDF8", // 🇺🇸
//            englishText = "The ring finger is traditionally the fourth finger of the hand."
//        ),
//        FactItem(
//            factNumber = 5,
//            foreignLang = "German",
//            foreignFlag = "\uD83C\uDDE9\uD83C\uDDEA", // 🇩🇪
//            foreignText = "Goldene Ringe sind in vielen Kulturen ein Symbol für Reichtum.",
//            englishFlag = "\uD83C\uDDFA\uD83C\uDDF8", // 🇺🇸
//            englishText = "Golden rings are a symbol of wealth in many cultures."
//        )
//    )
//    val sentences = listOf(
//        ExampleSentence(
//            number = 1,
//            foreignLang = "German",
//            foreignFlag = "\uD83C\uDDE9\uD83C\uDDEA", // 🇩🇪
//            foreignText = "Ich trage einen Ring am Finger.",
//            englishFlag = "\uD83C\uDDFA\uD83C\uDDF8", // 🇺🇸
//            englishText = "I wear a ring on my finger.",
//            ipa = "/ɪç ˈtʁaːɡə ˈaɪ̯nən ʁɪŋ am ˈfɪŋɐ/"
//        ),
//        ExampleSentence(
//            number = 2,
//            foreignLang = "German",
//            foreignFlag = "\uD83C\uDDE9\uD83C\uDDEA", // 🇩🇪
//            foreignText = "Der Ring ist aus Silber.",
//            englishFlag = "\uD83C\uDDFA\uD83C\uDDF8", // 🇺🇸
//            englishText = "The ring is made of silver.",
//            ipa = "/deːɐ̯ ʁɪŋ ɪst aʊs ˈzɪlbɐ/"
//        )
//    )
//    ARLearningScreen(
//        moduleTitle = "Learning German: Ring",
//        language = "German",
//        wordName = "Ring",
//        wordDifficulty = "Beginner",
//        article = "der",
//        translation = "Ring",
//        onSoundClick = {},
//        pronunciation = "/rɪŋ/",
//        category = "jewelry",
//        onSaveLearningPack = {},
//        isSaved = false,
//        onCompleteLearningPackClick = {},
//        sentencesCount = sentences.size,
//        tipsCount = tips.size,
//        factsCount = facts.size,
//        relatedCount = relatedWords.size,
//        relatedWords = relatedWords,
//        tips = tips,
//        facts = facts,
//        sentences = sentences,
//        onClose = {},
//        onScan = {}
//    )
//}

