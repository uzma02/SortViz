package com.kapps.mergesort.presentation.state

import androidx.compose.ui.graphics.Color
import com.kapps.mergesort.domain.model.QuickSortState

data class QuickSortInfoUiItem(
    val id: String,
    val low: Int,
    val high: Int,
    val list: List<Int>,
    val sortState: QuickSortState,
    val color: Color,
    val currentI: Int,
    val pivot: Int?
)
