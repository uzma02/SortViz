package com.kapps.mergesort.domain.model

data class SelectionSortInfo (
    val currentItem: Int,
    val shouldSwap: Boolean,
    val hadNoEffect: Boolean,
    val minItemIndex: Int

    )