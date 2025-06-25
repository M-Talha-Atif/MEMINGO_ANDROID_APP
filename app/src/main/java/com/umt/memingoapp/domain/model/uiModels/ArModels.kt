package com.umt.memingoapp.domain.model.uiModels

import kotlinx.serialization.Serializable


@Serializable
data class WordLearningContent(
    val word: String,
    val language: String,
    val translation: String,
    val pronunciation: String,
    val article: String? = null, // For languages with articles (German)
    val category: String,
    val difficulty: String,
    val sentences: List<ExampleSentence>,
    val tips: List<UsageTip>,
    val facts: List<FactItem>,
    val relatedWords: List<RelatedWordItem>
)

// Existing models (keep these)
@Serializable
data class ExampleSentence(
    val number: Int,
    val foreignLang: String,
    val foreignFlag: String,
    val foreignText: String,
    val englishFlag: String,
    val englishText: String,
    val ipa: String,
    @Transient val onSoundClick: () -> Unit = {}
//    val onSoundClick: () -> Unit = {}
)


@Serializable
data class UsageTip(
    val tipNumber: Int,
    val foreignLang: String,
    val foreignFlag: String,
    val foreignText: String,
    val englishFlag: String,
    val englishText: String
)

@Serializable
data class FactItem(
    val factNumber: Int,
    val foreignLang: String,
    val foreignFlag: String,
    val foreignText: String,
    val englishFlag: String,
    val englishText: String
)


@Serializable
data class RelatedWordItem(
    val word: String,
    val translation: String,
    @Transient val onSoundClick: () -> Unit = {}
//    val onSoundClick: () -> Unit = {}
)