package com.kapps.mergesort.presentation.state

import androidx.compose.ui.graphics.Color


data class BubbleListUiItem(
    val id:Int,
    val isCurrentlyCompared:Boolean,
    val value:Int,
    val color: Color

)
