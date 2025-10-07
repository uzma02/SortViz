package com.kapps.mergesort.data

import androidx.compose.ui.graphics.Color
import java.util.*

data class SortInformation(
    val id: String = UUID.randomUUID().toString(),
    val color: Color,
    val sortParts: List<List<Int>>
)
