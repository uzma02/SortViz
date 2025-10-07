package com.kapps.mergesort.domain

import android.util.Log
import com.kapps.mergesort.domain.model.SortInfo
import com.kapps.mergesort.domain.model.SortState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.UUID

class MergeSortUseCase {

    val sortFlow = MutableSharedFlow<SortInfo>()

    suspend operator fun invoke(list: List<Int>, depth:Int):List<Int> {

        sortFlow.emit(SortInfo(
            id = UUID.randomUUID().toString(),
            depth = depth,
            sortParts = list,
            sortState = SortState.DIVIDED,
        ))
        delay(4000)
        val listSize = list.size
        if (listSize <= 1) {
            return list
        }

        var leftList = list.slice(0 until (listSize + 1) / 2)
        var rightList = list.slice((listSize + 1) / 2 until listSize)
        leftList = this(leftList, depth + 1)
        rightList = this(rightList,depth + 1)
        return merge(leftList.toMutableList(), rightList.toMutableList(), depth)
    }

    private suspend fun merge(leftList:MutableList<Int>, rightList:MutableList<Int>, depth:Int):List<Int>{

        val mergeList = mutableListOf<Int>()
        while (leftList.isNotEmpty() && rightList.isNotEmpty()){

            if(leftList.first() <= rightList.first()){
                mergeList.add(mergeList.size,leftList.removeFirst())
            }else{
                mergeList.add(mergeList.size,rightList.removeFirst())
            }
        }

        mergeList.addAll(leftList)
        delay(4000)
        mergeList.addAll(rightList)

        sortFlow.emit(SortInfo(
            UUID.randomUUID().toString(),
            depth = depth,
            sortParts = mergeList,
            sortState = SortState.MERGED,
        ))

        return mergeList
    }
}

