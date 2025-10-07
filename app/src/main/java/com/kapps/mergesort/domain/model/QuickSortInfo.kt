package com.kapps.mergesort.domain.model

data class QuickSortInfo(
    val id: String,
    val low: Int,
    val high: Int,
    val list: List<Int>,
    val sortState: QuickSortState,
    val pivot: Int?,  // Nullable to handle cases when no pivot is selected
    val currentLine: Int
)
