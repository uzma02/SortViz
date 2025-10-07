package com.kapps.mergesort.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapps.mergesort.domain.MergeSortUseCase
import com.kapps.mergesort.domain.model.SortState
import com.kapps.mergesort.presentation.state.MergeSortInfoUiItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class MergeSortViewPseudo(
    private val mergeSortUseCase: MergeSortUseCase = MergeSortUseCase()
) : ViewModel() {

    var listToSort = mutableListOf<Int>()

    var mergeSortInfoUiItemList = mutableStateListOf<MergeSortInfoUiItem>()
    var currentPseudocodeStep = mutableStateOf(0)

    init {
        for (i in 0 until 8) {
            listToSort.add(
                (10..99).random()
            )
        }
    }



    fun startSortingPseudo() {
        mergeSortInfoUiItemList.clear()
        subscribeToSortChanges()
        viewModelScope.launch {
            mergeSort(listToSort, 0, listToSort.size - 1, 0)
        }
    }

    private var job: Job? = null
    private fun subscribeToSortChanges() {
        job?.cancel()
        job = viewModelScope.launch {
            mergeSortUseCase.sortFlow.collect { sortInfo ->
                val depthAlreadyExistListIndex = mergeSortInfoUiItemList.indexOfFirst {
                    it.depth == sortInfo.depth && it.sortState == sortInfo.sortState
                }

                if (depthAlreadyExistListIndex == -1) {
                    mergeSortInfoUiItemList.add(
                        MergeSortInfoUiItem(
                            id = UUID.randomUUID().toString(),
                            depth = sortInfo.depth,
                            sortState = sortInfo.sortState,
                            sortParts = listOf(sortInfo.sortParts),
                            color = Color(
                                (0..255).random(),
                                (0..200).random(),
                                (0..200).random(),
                                255)
                        )
                    )
                } else {
                    val currentPartList = mergeSortInfoUiItemList[depthAlreadyExistListIndex].sortParts.toMutableList()
                    currentPartList.add(sortInfo.sortParts)
                    mergeSortInfoUiItemList[depthAlreadyExistListIndex] =
                        mergeSortInfoUiItemList[depthAlreadyExistListIndex].copy(sortParts = currentPartList)
                }

                mergeSortInfoUiItemList.sortWith(
                    compareBy(
                        { it.sortState },
                        { it.depth }
                    )
                )
            }
        }
    }

    private suspend fun mergeSort(arr: MutableList<Int>, l: Int, r: Int, depth: Int) {
        if (l >= r) {
            updatePseudocodeStep(1) // if left > right, return
            return
        }

        updatePseudocodeStep(2) // mid = (left + right) / 2
        val m = l + (r - l) / 2

        updatePseudocodeStep(3) // mergeSort(arr, left, mid)
        mergeSort(arr, l, m, depth + 1)

        updatePseudocodeStep(4) // mergeSort(arr, mid + 1, right)
        mergeSort(arr, m + 1, r, depth + 1)

        updatePseudocodeStep(5) // merge(arr, left, mid, right)
        merge(arr, l, m, r)

        // Update UI state for merging
        updateUiState(arr, l, r, depth, SortState.MERGED)
    }

    private suspend fun merge(arr: MutableList<Int>, l: Int, m: Int, r: Int) {
        val n1 = m - l + 1
        val n2 = r - m

        val L = mutableListOf<Int>()
        val R = mutableListOf<Int>()

        for (i in 0 until n1) {
            L.add(arr[l + i])
        }
        for (j in 0 until n2) {
            R.add(arr[m + 1 + j])
        }

        var i = 0
        var j = 0
        var k = l

        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i]
                i++
            } else {
                arr[k] = R[j]
                j++
            }
            k++
        }

        while (i < n1) {
            arr[k] = L[i]
            i++
            k++
        }

        while (j < n2) {
            arr[k] = R[j]
            j++
            k++
        }
    }

    private fun updateUiState(arr: MutableList<Int>, l: Int, r: Int, depth: Int, sortState: SortState) {
        val sublist = arr.subList(l, r + 1)
        val sortParts = when (sortState) {
            SortState.DIVIDED -> listOf(sublist)
            SortState.MERGED -> listOf(sublist)
        }

        mergeSortInfoUiItemList.add(
            MergeSortInfoUiItem(
                id = UUID.randomUUID().toString(),
                depth = depth,
                sortState = sortState,
                sortParts = sortParts,
                color = Color((0..255).random(), (0..200).random(), (0..200).random(), 255)
            )
        )
    }

    private suspend fun updatePseudocodeStep(step: Int) {
        currentPseudocodeStep.value = step
        delay(1900) // Adjust the delay time as needed
    }
}
