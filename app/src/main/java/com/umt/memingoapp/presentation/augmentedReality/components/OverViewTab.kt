package com.umt.memingoapp.presentation.augmentedReality.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umt.memingoapp.domain.model.uiModels.WordLearningContent

@Composable
fun OverviewTab(content: WordLearningContent) {
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