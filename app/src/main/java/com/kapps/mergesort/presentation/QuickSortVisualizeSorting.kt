package com.kapps.mergesort.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kapps.mergesort.domain.model.QuickSortInfo
import com.kapps.mergesort.domain.model.QuickSortState
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Composable
fun QuickSortVisualizeSorting(sortFlow: Flow<QuickSortInfo>) {
    val sortState by sortFlow.collectAsState(initial = QuickSortInfo(
        id = UUID.randomUUID().toString(),
        low = -1,
        high = -1,
        list = listOf(),
        sortState = QuickSortState.IN_PROGRESS,
        pivot = null,  // Allow pivot to be nullable
        currentLine = 1
    ))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Render the sorting boxes
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            sortState.list.forEachIndexed { index, number ->
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(4.dp)
                        .background(
                            color = when {
                                index == sortState.low -> Color.Red
                                index == sortState.high -> Color.Blue
                                index == sortState.pivot -> Color.Green
                                else -> Color.Gray
                            },
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number.toString(),
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display pointers and pivot
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if (sortState.low in sortState.list.indices) {
                QuickArrowPointer("low", sortState.low, sortState.list, Color.Red)
            }
            if (sortState.high in sortState.list.indices) {
                QuickArrowPointer("high", sortState.high, sortState.list, Color.Blue)
            }
            sortState.pivot?.let {
                if (it in sortState.list.indices) {
                    KeyPointer("pivot", it, sortState.list[it], Color.Green)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Render pseudocode and highlight the currently executing line
        QuickPseudoCode(currentLine = sortState.currentLine)
    }
}

@Composable
fun QuickPseudoCode(currentLine: Int) {
    // Pseudocode for quickSort and partition function
    val pseudocode = listOf(
        "void quickSort(arr[], low, high) {",          // 1
        "    if (low < high) {",                       // 2
        "        int pi = partition(arr, low, high);", // 3
        "        quickSort(arr, low, pi - 1);",        // 4
        "        quickSort(arr, pi + 1, high);",       // 5
        "    }",                                       // 6
        "}",                                           // 7
        "int partition(arr[], low, high) {",           // 8
        "    int pivot = arr[high];",                  // 9
        "    int i = (low - 1);",                      // 10
        "    for (int j = low; j < high; j++) {",      // 11
        "        if (arr[j] < pivot) {",               // 12
        "            i++;",                            // 13
        "            swap(arr[i], arr[j]);",           // 14
        "        }",                                   // 15
        "    }",                                       // 16
        "    swap(arr[i + 1], arr[high]);",            // 17
        "    return (i + 1);",                         // 18
        "}"                                            // 19
    )

    // Highlight the lines with transparent white background and yellow text
    Column {
        pseudocode.forEachIndexed { index, line ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(
                        if (index + 1 == currentLine) Color.White.copy(alpha = 0.3f) // Transparent white box for current line
                        else Color.Transparent,
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                Text(
                    text = line,
                    color = if (index + 1 == currentLine) Color.Yellow else Color.White, // Yellow for the current line text
                    fontSize = 16.sp,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}


@Composable
fun KeyPointer(pointerLabel: String, index: Int, key: Int, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (index >= 0) {
            Text(pointerLabel, color = color, fontSize = 18.sp)  // Use the provided color
            Spacer(modifier = Modifier.height(4.dp))
            Text("↓", color = color, fontSize = 18.sp)  // Use the provided color
            Spacer(modifier = Modifier.height(4.dp))
            Text(key.toString(), color = color, fontSize = 20.sp)  // Use the provided color
        } else {
            Text("Index out of bounds", color = Color.Red, fontSize = 18.sp)
        }
    }
    Spacer(modifier = Modifier.width(16.dp))
}

@Composable
fun QuickArrowPointer(pointerLabel: String, index: Int, list: List<Int>, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (index in list.indices) {
            Text(pointerLabel, color = color, fontSize = 18.sp)  // Use the provided color
            Spacer(modifier = Modifier.height(4.dp))
            Text("↓", color = color, fontSize = 18.sp)  // Use the provided color
            Spacer(modifier = Modifier.height(4.dp))
            Text(list[index].toString(), color = color, fontSize = 18.sp)  // Use the provided color
        } else {
            Text("Index out of bounds", color = Color.Red, fontSize = 18.sp)
        }
    }
    Spacer(modifier = Modifier.width(16.dp))
}

















