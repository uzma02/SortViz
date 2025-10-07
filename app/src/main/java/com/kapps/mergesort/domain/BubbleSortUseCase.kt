package com.kapps.mergesort.domain

import com.kapps.mergesort.domain.model.BubbleSortInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BubbleSortUseCase {

    operator fun invoke(arr: MutableList<Int>): Flow<BubbleSortInfo> = flow {
        val n = arr.size

        for (i in 0 until n - 1) {
            var swapped = false

            // Iterate through the list, comparing adjacent elements
            for (j in 0 until n - i - 1) {
                emit(
                    BubbleSortInfo(currentItem = j, shouldSwap = false, hadNoEffect = false)
                )
                delay(800)

                if (arr[j] > arr[j + 1]) {
                    // Swap if the current item is greater than the next item
                    val temp = arr[j]
                    arr[j] = arr[j + 1]
                    arr[j + 1] = temp
                    swapped = true

                    emit(
                        BubbleSortInfo(currentItem = j, shouldSwap = true, hadNoEffect = false)
                    )
                } else {
                    // No swap was needed
                    emit(
                        BubbleSortInfo(currentItem = j, shouldSwap = false, hadNoEffect = true)
                    )
                }

                delay(500)
            }

            // If no two elements were swapped, the list is already sorted
            if (!swapped) {
                break
            }
        }
    }
}
