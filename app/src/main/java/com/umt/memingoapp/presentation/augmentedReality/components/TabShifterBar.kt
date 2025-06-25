package com.umt.memingoapp.presentation.augmentedReality.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.umt.memingoapp.ui.theme.LogoColor

enum class TabType {
    Overview, Sentences, Tips, Facts
}





@Composable
fun TabShifterBar(
    selectedTab: TabType,
    onTabSelected: (TabType) -> Unit,
    modifier: Modifier = Modifier
) {
    val allTabs = listOf(
        TabType.Overview,
        TabType.Sentences,
        TabType.Tips,
        TabType.Facts
    )
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(allTabs) { tab ->
            TabShifterButton(
                icon = when(tab) {
                    TabType.Overview -> Icons.Filled.Info
                    TabType.Sentences -> Icons.Filled.ChatBubbleOutline
                    TabType.Tips -> Icons.Filled.Lightbulb
                    TabType.Facts -> Icons.Filled.Star
                },
                label = tab.name.replaceFirstChar { it.uppercase() },
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}

@Composable
fun TabShifterButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) LogoColor.copy(alpha = 0.10f) else Color(0xFFF6F7FA)
    val contentColor = if (selected) LogoColor else Color(0xFF7A7A7A)
    val fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 17.dp, vertical = 10.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(7.dp))
        Text(
            label,
            color = contentColor,
            fontWeight = fontWeight,
            fontSize = 17.sp,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFF6F7FA)
fun TabShifterBarPreview() {
    var selectedTab by remember { mutableStateOf(TabType.Overview) }
    Column(Modifier.background(Color(0xFFF6F7FA))) {
        TabShifterBar(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
    }
}