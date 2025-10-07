package com.kapps.mergesort.domain

import com.kapps.mergesort.domain.model.QuickSortInfo
import com.kapps.mergesort.domain.model.QuickSortState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.UUID

class QuickSortUseCase {

    val sortFlow = MutableSharedFlow<QuickSortInfo>()

    suspend operator fun invoke(list: List<Int>, delayMillis: Long = 500) {
        val arr = list.toMutableList()
        quickSort(arr, 0, arr.size - 1, delayMillis)
        sortFlow.emit(
            QuickSortInfo(
                id = UUID.randomUUID().toString(),
                low = 0,
                high = arr.size - 1,
                list = arr.toList(),
                sortState = QuickSortState.COMPLETED,
                pivot = null,
                currentLine = -1  // No current line after completion
            )
        )
    }

    private suspend fun quickSort(arr: MutableList<Int>, low: Int, high: Int, delayMillis: Long) {
        emitState(arr, low, high, null, 1)  // Line 1 of pseudocode (quickSort function start)
        delay(delayMillis)

        if (low < high) {
            emitState(arr, low, high, null, 2)  // Line 2: if (low < high)
            delay(delayMillis)

            val pivotIndex = partition(arr, low, high, delayMillis)
            delay(delayMillis)

            emitState(arr, low, high, null, 4)  // Line 4: quickSort(arr, low, pi - 1)
            quickSort(arr, low, pivotIndex - 1, delayMillis)

            emitState(arr, low, high, null, 5)  // Line 5: quickSort(arr, pi + 1, high)
            quickSort(arr, pivotIndex + 1, high, delayMillis)
        }

        emitState(arr, low, high, null, 6)  // Line 6: End of quickSort function
        delay(delayMillis)
    }

    private suspend fun partition(arr: MutableList<Int>, low: Int, high: Int, delayMillis: Long): Int {
        val pivot = arr[high]
        emitState(arr, low, high, pivot, 9)  // Line 9 of pseudocode (pivot assignment)
        delay(delayMillis)

        var i = low - 1
        emitState(arr, low, high, pivot, 10)  // Line 10: i = low - 1
        delay(delayMillis)

        for (j in low until high) {
            emitState(arr, low, high, pivot, 11)  // Line 11: for (j in low until high)
            delay(delayMillis)

            if (arr[j] <= pivot) {
                i++
                arr.swap(i, j)
                emitState(arr, low, high, pivot, 13)  // Line 13: i++ and swap
                delay(delayMillis)
            }
        }

        arr.swap(i + 1, high)
        emitState(arr, low, high, pivot, 17)  // Line 17: Final swap
        delay(delayMillis)

        emitState(arr, low, high, pivot, 18)  // Line 18: Return pivot index
        delay(delayMillis)

        return i + 1
    }

    private suspend fun emitState(arr: List<Int>, low: Int, high: Int, pivot: Int?, currentLine: Int) {
        sortFlow.emit(
            QuickSortInfo(
                id = UUID.randomUUID().toString(),
                low = low,
                high = high,
                list = arr.toList(),
                sortState = QuickSortState.IN_PROGRESS,
                pivot = pivot,
                currentLine = currentLine  // Sync the current line with pseudocode
            )
        )
    }

    private fun MutableList<Int>.swap(i: Int, j: Int) {
        val temp = this[i]
        this[i] = this[j]
        this[j] = temp
    }
}

