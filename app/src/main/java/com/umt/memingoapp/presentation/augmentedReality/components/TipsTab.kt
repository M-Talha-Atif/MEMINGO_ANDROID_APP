package com.umt.memingoapp.presentation.augmentedReality.components

import androidx.compose.runtime.Composable
import com.umt.memingoapp.domain.model.uiModels.UsageTip

@Composable
fun TipsTab(tips: List<UsageTip>) {
    UsageTipsList(tips = tips)
}
