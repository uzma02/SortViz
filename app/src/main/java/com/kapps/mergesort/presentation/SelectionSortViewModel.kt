package com.kapps.mergesort.presentation

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapps.mergesort.com.kapps.mergesort.presentation.state.SelectionListUiItem
import com.kapps.mergesort.domain.SelectionSortUseCase
import com.kapps.mergesort.domain.swap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class SelectionSortViewModel(
    private val selectionSortUseCase: SelectionSortUseCase = SelectionSortUseCase()
) : ViewModel() {
    /*private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp
    }*/

    var currentStep by mutableStateOf(0)
    var listToSort = mutableStateListOf<SelectionListUiItem>()

    fun initializeListWithInput(input: String) {
        listToSort.clear()
        val numbers = input.split(",").map { it.trim() }
        for ((index, numberString) in numbers.withIndex()) {
            val rnd = Random()
            val number = numberString.toIntOrNull() ?: continue
            val listUiItem = SelectionListUiItem(
                id = index,
                isCurrentlyCompared = false,
                value = number,
                color = Color(
                    255,
                    rnd.nextInt(256),
                    rnd.nextInt(256),
                    255
                )
            )
            listToSort.add(listUiItem)
        }
    }

    fun startSorting() {
        viewModelScope.launch {
            val values = listToSort.map { it.value }.toMutableList()

            for (i in 0 until values.size - 1) {
                currentStep = 1 // Highlight the "for i = 0 to n-1:" step
                var minIndex = i
                delay(800)

                for (j in i + 1 until values.size) {
                    currentStep = 3 // Highlight the "for j = i+1 to n:" step

                    // Highlight the elements being compared
                    listToSort.forEachIndexed { index, bubbleListUiItem ->
                        listToSort[index] = bubbleListUiItem.copy(isCurrentlyCompared = index == i || index == j)
                    }
                    delay(1100)

                    if (values[j] < values[minIndex]) {
                        minIndex = j
                    }
                    currentStep = 5 // Highlight the comparison "if arr[j] < arr[minIndex]:"
                }

                if (minIndex != i) {
                    currentStep = 7 // Highlight the "swap(arr[i], arr[minIndex])" step
                    values.swap(i, minIndex)
                    listToSort.selectionswap(i, minIndex)
                    delay(800)
                }

                currentStep = 8 // Highlight the end of the outer loop
            }

            currentStep = 0 // Reset to the initial step once sorting is done
        }
    }


    private fun MutableList<SelectionListUiItem>.selectionswap(index1: Int, index2: Int) {
        val temp = this[index1].value
        this[index1] = this[index1].copy(value = this[index2].value)
        this[index2] = this[index2].copy(value = temp)
    }


}
