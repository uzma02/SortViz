package com.kapps.mergesort.domain.model

data class BubbleSortInfo(
    val currentItem:Int,
    val shouldSwap:Boolean,
    val hadNoEffect:Boolean
)

