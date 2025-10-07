package com.kapps.mergesort.domain

import com.kapps.mergesort.domain.model.SelectionSortInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SelectionSortUseCase {

    operator fun invoke(arr: MutableList<Int>): Flow<SelectionSortInfo> = flow {
        val n = arr.size

        for (i in 0 until n - 1) {
            var minIndex = i

            // Find minimum element in the unsorted part of the array
            for (j in i + 1 until n) {
                emit(
                    SelectionSortInfo(currentItem = j, minItemIndex = minIndex, shouldSwap = false, hadNoEffect = false)
                )
                delay(800)
                if (arr[j] < arr[minIndex]) {
                    minIndex = j
                }
            }

            // Swap minimum element with the current element
            if (i != minIndex) {
                val temp = arr[minIndex]
                arr[minIndex] = arr[i]
                arr[i] = temp
                emit(
                    SelectionSortInfo(currentItem = i, minItemIndex = minIndex, shouldSwap = true, hadNoEffect = false)
                )
            } else {
                emit(
                    SelectionSortInfo(currentItem = i, minItemIndex = minIndex, shouldSwap = false, hadNoEffect = true)
                )
            }

            delay(500)
        }
    }
}

