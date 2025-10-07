package com.kapps.mergesort.presentation



import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapps.mergesort.domain.QuickSortUseCase
import com.kapps.mergesort.domain.model.QuickSortInfo
import com.kapps.mergesort.domain.model.SortInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class QuickSortViewModel : ViewModel() {

    var listToSort = mutableStateListOf<Int>()
        set(value) {
            field = value
        }

    private val quickSortUseCase = QuickSortUseCase()

    // This function triggers the sorting process and syncs the flow with UI
    fun startSorting(delayMillis: Long = 500L) {
        viewModelScope.launch {
            // Trigger the quickSort logic from use case
            quickSortUseCase(listToSort.toList(), delayMillis)
        }
    }

    // Function to get the flow from QuickSortUseCase to observe in the UI
    fun getSortFlow(): MutableSharedFlow<QuickSortInfo> {
        return quickSortUseCase.sortFlow
    }
}




