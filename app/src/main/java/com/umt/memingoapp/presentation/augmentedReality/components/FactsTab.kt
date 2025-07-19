package com.umt.memingoapp.presentation.augmentedReality.components

import androidx.compose.runtime.Composable
import com.umt.memingoapp.domain.model.uiModels.FactItem

@Composable
 fun FactsTab(facts: List<FactItem>) {
    FactList(facts = facts)
}