package com.kapps.mergesort.domain.model


data class InsertionSortInfo(
    val id: String,
    val index: Int,
    val key: Int,
    val list: List<Int>,
    val sortState: InsertionSortState,
    val j: Int,
    val j1: Int,
    val currentLine: Int // Add this line
)
