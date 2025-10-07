package com.kapps.mergesort.com.kapps.mergesort.presentation.state

import androidx.compose.ui.graphics.Color

data class SelectionListUiItem (

        val id:Int,
        val isCurrentlyCompared:Boolean,
        val value:Int,
        val color: Color

    )