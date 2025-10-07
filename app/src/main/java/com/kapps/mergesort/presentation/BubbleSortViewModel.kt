package com.kapps.mergesort.presentation

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapps.mergesort.domain.BubbleSortUseCase
import com.kapps.mergesort.domain.swap
import com.kapps.mergesort.presentation.state.BubbleListUiItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class SortViewModel(
    private val bubbleSortUseCase: BubbleSortUseCase = BubbleSortUseCase()
) : ViewModel() {

    var currentStep by mutableStateOf(0)
    var listToSort = mutableStateListOf<BubbleListUiItem>()

    fun initializeListWithInput(input: String) {
        listToSort.clear()
        val numbers = input.split(",").map { it.trim() }
        for ((index, numberString) in numbers.withIndex()) {
            val rnd = Random()
            val number = numberString.toIntOrNull() ?: continue
            val listUiItem = BubbleListUiItem(
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

            for (i in 0 until values.size-1) {
                currentStep = 1 // Highlight the "for i = 0 to n-1:" step
                var swapped = false

                for (j in 0 until values.size - i - 1) {
                    currentStep = 2 // Highlight the inner loop step

                    // Highlight the elements being compared
                    listToSort.forEachIndexed { index, bubbleListUiItem ->
                        listToSort[index] = bubbleListUiItem.copy(isCurrentlyCompared = index == j || index == j + 1)
                    }
                    delay(1100)

                    if (values[j] > values[j + 1]) {
                        currentStep = 3 // Highlight the comparison "if arr[j] > arr[j + 1]:"
                        values.swap(j, j + 1)
                        listToSort.bubbleswap(j, j + 1)
                        swapped = true
                        delay(800)
currentStep = 4
                    }
                }

                currentStep = 5 // Highlight the end of the outer loop

                if (!swapped) {
                    break // No swaps means the list is sorted
                }
            }

            currentStep = 0 // Reset to the initial step once sorting is done
        }
    }

    private fun MutableList<BubbleListUiItem>.bubbleswap(index1: Int, index2: Int) {
        val temp = this[index1].value
        this[index1] = this[index1].copy(value = this[index2].value)
        this[index2] = this[index2].copy(value = temp)
    }
}
