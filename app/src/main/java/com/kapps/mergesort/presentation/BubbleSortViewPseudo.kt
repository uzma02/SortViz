package com.kapps.mergesort.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapps.mergesort.domain.model.SortState
import com.kapps.mergesort.presentation.state.BubbleListUiItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class BubbleSortViewPseudo : ViewModel() {

    var listToSort = mutableListOf<Int>()
    var bubbleSortInfoUiItemList = mutableStateListOf<BubbleListUiItem>()
    var currentPseudocodeStep = mutableStateOf(0)

    init {
        for (i in 0 until 8) {
            listToSort.add((10..99).random())
        }
    }

    fun startSortingPseudo() {
        bubbleSortInfoUiItemList.clear()
        // Initialize the UI list with values from listToSort
        listToSort.forEachIndexed { index, value ->
            bubbleSortInfoUiItemList.add(
                BubbleListUiItem(
                    id = index,
                    isCurrentlyCompared = false,
                    value = value,
                    color = Color.Gray // Initial color
                )
            )
        }

        viewModelScope.launch {
            bubbleSort(listToSort)
        }
    }

    private suspend fun bubbleSort(arr: MutableList<Int>) {
        val n = arr.size
        for (i in 0 until n - 1) {
            var swapped = false
            //updatePseudocodeStep(1) // Step: Initialize outer loop (for i)

            for (j in 0 until n - i - 1) {
                //updatePseudocodeStep(2) // Step: Initialize inner loop (for j)

                val currentItem = arr[j]
                val nextItem = arr[j + 1]

                // Update UI state for comparison (highlighting the elements being compared)
                bubbleSortInfoUiItemList[j] = bubbleSortInfoUiItemList[j].copy(
                    isCurrentlyCompared = true,
                    color = Color.Gray // Color for comparison
                )
                bubbleSortInfoUiItemList[j + 1] = bubbleSortInfoUiItemList[j + 1].copy(
                    isCurrentlyCompared = true,
                    color = Color.Gray // Color for comparison
                )

                delay(500) // Slow down to visualize comparison

                updatePseudocodeStep(3) // Step: Compare arr[j] and arr[j + 1]

                if (currentItem > nextItem) {
                    // Update pseudocode to highlight the swap
                    updatePseudocodeStep(4) // Step: Swap elements (highlight swap line)

                    // Swap values in arr
                    arr[j] = arr[j + 1]
                    arr[j + 1] = currentItem
                    swapped = true

                    // Update UI after swap
                    bubbleSortInfoUiItemList[j] = bubbleSortInfoUiItemList[j].copy(
                        isCurrentlyCompared = false,
                        value = arr[j], // Update value after swap
                        color = Color.Red // Color for swap
                    )
                    bubbleSortInfoUiItemList[j + 1] = bubbleSortInfoUiItemList[j + 1].copy(
                        isCurrentlyCompared = false,
                        value = arr[j + 1], // Update value after swap
                        color = Color.Red // Color for swap
                    )

                } else {
                    // If no swap, update the UI to reflect this
                    bubbleSortInfoUiItemList[j] = bubbleSortInfoUiItemList[j].copy(
                        isCurrentlyCompared = false,
                        color = Color.Green // Color for no swap
                    )
                    bubbleSortInfoUiItemList[j + 1] = bubbleSortInfoUiItemList[j + 1].copy(
                        isCurrentlyCompared = false,
                        color = Color.Green // Color for no swap
                    )
                }

                delay(500) // Slow down to visualize swap or no effect

                // Update pseudocode step after the swap or comparison
                updatePseudocodeStep(5) // Move to next iteration
            }

            if (!swapped) {
                updatePseudocodeStep(6) // Step: No swap, break the loop
                break
            }

            updatePseudocodeStep(7) // Step: End of outer loop iteration
        }

        // Mark the list as sorted with blue color
        bubbleSortInfoUiItemList.forEachIndexed { index, item ->
            bubbleSortInfoUiItemList[index] = item.copy(color = Color.Blue) // Color for sorted state
        }

        updatePseudocodeStep(8) // Step: Sorting completed
    }



    private suspend fun updatePseudocodeStep(step: Int) {
        currentPseudocodeStep.value = step
        delay(900) // Adjust the delay time as needed
    }
}
