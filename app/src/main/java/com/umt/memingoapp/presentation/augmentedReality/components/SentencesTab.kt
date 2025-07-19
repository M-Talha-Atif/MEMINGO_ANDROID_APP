package com.umt.memingoapp.presentation.augmentedReality.components

import androidx.compose.runtime.Composable
import com.umt.memingoapp.domain.model.uiModels.ExampleSentence

@Composable
fun SentencesTab(sentences: List<ExampleSentence>) {
    ExampleSentencesList(sentences = sentences)
}