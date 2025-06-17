package com.umt.memingoapp.featureAr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ARLearningScreen(
    moduleTitle: String,
    language: String,
    wordName: String,
    wordDifficulty: String,
    article: String,
    translation: String,
    onSoundClick: () -> Unit,
    pronunciation: String,
    category: String,
    onSaveLearningPack: () -> Unit,
    isSaved: Boolean,
    onCompleteLearningPackClick: () -> Unit,
    sentencesCount: Int,
    tipsCount: Int,
    factsCount: Int,
    relatedCount: Int,
    relatedWords: List<RelatedWordItem>,
    tips: List<UsageTip>,
    facts: List<FactItem>,
    sentences: List<ExampleSentence>,
    onClose: () -> Unit,
    onScan: () -> Unit,
) {
    var selectedTab by remember { mutableStateOf(TabType.Overview) }
    Surface(color = Color(0xFFF6F7FA)) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F7FA))
                .padding(bottom = 0.dp)
        ) {
            ARLearningTopBar(
                moduleTitle = moduleTitle,
                language = language,
                onClose = onClose,
                onScan = onScan
            )
            TabShifterBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
            )
            when (selectedTab) {
                TabType.Overview -> {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        WordLearningCard(
                            wordName = wordName,
                            wordDifficulty = wordDifficulty,
                            article = article,
                            translation = translation,
                            onSoundClick = onSoundClick,
                            pronunciation = pronunciation,
                            category = category,
                            language = language,
                            onSaveLearningPack = onSaveLearningPack,
                            isSaved = isSaved,
                            onCompleteLearningPackClick = onCompleteLearningPackClick,
                            sentencesCount = sentencesCount,
                            tipsCount = tipsCount,
                            factsCount = factsCount,
                            relatedCount = relatedCount,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 4.dp)
                        )
                        RelatedWordsCard(
                            relatedWords = relatedWords,
                            modifier = Modifier
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                }
                TabType.Sentences -> {
                    ExampleSentencesList(
                        sentences = sentences,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .weight(1f, fill = false)
                    )
                }
                TabType.Tips -> {
                    UsageTipsList(
                        tips = tips,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .weight(1f, fill = false)
                    )
                }
                TabType.Facts -> {
                    FactList(
                        facts = facts,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .weight(1f, fill = false)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F7FA)
@Composable
fun ARLearningScreenPreview() {
    // Dummy data
    val relatedWords = listOf(
        RelatedWordItem("Schmuck", "jewelry"),
        RelatedWordItem("Ehering", "wedding ring"),
        RelatedWordItem("Silber", "silver")
    )
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
        ),
        UsageTip(
            tipNumber = 3,
            foreignLang = "German",
            foreignFlag = "\uD83C\uDDE9\uD83C\uDDEA", // 🇩🇪
            foreignText = "Ein Ring kann ein Symbol für Ehe oder Freundschaft sein.",
            englishFlag = "\uD83C\uDDFA\uD83C\uDDF8", // 🇺🇸
            englishText = "A ring can be a symbol of marriage or friendship."
        )
    )
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
    ARLearningScreen(
        moduleTitle = "Learning German: Ring",
        language = "German",
        wordName = "Ring",
        wordDifficulty = "Beginner",
        article = "der",
        translation = "Ring",
        onSoundClick = {},
        pronunciation = "/rɪŋ/",
        category = "jewelry",
        onSaveLearningPack = {},
        isSaved = false,
        onCompleteLearningPackClick = {},
        sentencesCount = sentences.size,
        tipsCount = tips.size,
        factsCount = facts.size,
        relatedCount = relatedWords.size,
        relatedWords = relatedWords,
        tips = tips,
        facts = facts,
        sentences = sentences,
        onClose = {},
        onScan = {}
    )
}