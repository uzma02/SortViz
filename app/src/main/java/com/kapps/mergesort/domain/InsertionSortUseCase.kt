package com.kapps.mergesort.domain

import com.kapps.mergesort.domain.model.InsertionSortInfo
import com.kapps.mergesort.domain.model.InsertionSortState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.UUID

class InsertionSortUseCase {

    val sortFlow = MutableSharedFlow<InsertionSortInfo>()

    suspend operator fun invoke(list: List<Int>, delayMillis: Long = 500) { // Visualization speed controlled by delayMillis
        val arr = list.toMutableList()
        val n = arr.size

        for (i in 1 until n) {
            val key = arr[i]
            var j = i - 1

            sortFlow.emit(InsertionSortInfo(
                id = UUID.randomUUID().toString(),
                index = i,
                key = key,
                list = arr.toList(),
                sortState = InsertionSortState.IN_PROGRESS,
                j = j,
                j1 = j + 1,
                currentLine = 1 // Line 1 of pseudocode: for i from 1 to n-1 do
            ))

            delay(delayMillis) // Visualization speed

            sortFlow.emit(InsertionSortInfo(
                id = UUID.randomUUID().toString(),
                index = i,
                key = key,
                list = arr.toList(),
                sortState = InsertionSortState.IN_PROGRESS,
                j = j,
                j1 = j + 1,
                currentLine = 2 // Line 2 of pseudocode: key ← arr[i]
            ))

            delay(delayMillis)

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j]
                j = j - 1

                sortFlow.emit(InsertionSortInfo(
                    id = UUID.randomUUID().toString(),
                    index = j + 1,
                    key = key,
                    list = arr.toList(),
                    sortState = InsertionSortState.IN_PROGRESS,
                    j = j,
                    j1 = j + 1,
                    currentLine = 5 // Line 5 of pseudocode: while j ≥ 0 and arr[j] > key do
                ))

                delay(delayMillis) // Visualization speed
            }

            arr[j + 1] = key

            sortFlow.emit(InsertionSortInfo(
                id = UUID.randomUUID().toString(),
                index = j + 1,
                key = key,
                list = arr.toList(),
                sortState = InsertionSortState.IN_PROGRESS,
                j = j,
                j1 = j + 1,
                currentLine = 8 // Line 8 of pseudocode: arr[j + 1] ← key
            ))

            delay(delayMillis)
        }

        sortFlow.emit(InsertionSortInfo(
            id = UUID.randomUUID().toString(),
            index = n,
            key = -1,
            list = arr.toList(),
            sortState = InsertionSortState.COMPLETED,
            j = -1,
            j1 = -1,
            currentLine = -1 // Sorting completed, no line highlighted
        ))
    }
}