package com.kapps.mergesort

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kapps.mergesort.presentation.QuickSortViewModel
import com.kapps.mergesort.presentation.QuickSortVisualizeSorting
import com.kapps.mergesort.ui.theme.QuickSortTheme

class QuickSortActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickSortTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF222222)
                ) {
                    val viewModel = QuickSortViewModel()
                    QuickSortScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun QuickSortScreen(viewModel: QuickSortViewModel) {
    var input by remember { mutableStateOf("") }
    var delayMillis by remember { mutableStateOf(500L) }
    var showTimeComplexity by remember { mutableStateOf(false) }
    var showGraph by remember { mutableStateOf(false) }
    var worstCaseInputSize by remember { mutableStateOf(10) } // Independent state for Worst Case
    var bestCaseInputSize by remember { mutableStateOf(10) }  // Independent state for Best Case
    var spaceComplexityInputSize by remember { mutableStateOf(10) } // Separate for Space Complexity
    var spaceComplexityInputSizeWorst by remember { mutableStateOf(10) } // Separate for Worst Space Complexity
    var spaceComplexityInputSizeBest by remember { mutableStateOf(10) }  // Separate for Best Space Complexity
    var showDescription by remember { mutableStateOf(false) }
    var showSpaceComplexity by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Visualize Sorting
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QuickSortVisualizeSorting(sortFlow = viewModel.getSortFlow())
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Field
        Text(
            "Enter numbers separated by space",
            color = Color.Magenta,
            fontSize = 18.sp
        )

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            singleLine = true
        )

        // Delay Input
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Delay (ms):")
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = delayMillis.toString(),
                onValueChange = { delayMillis = it.toLongOrNull() ?: 500L },
                modifier = Modifier.width(100.dp),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons for Starting Sort and Showing Time Complexity
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    val numbers = input.split(" ").mapNotNull { it.toIntOrNull() }
                    viewModel.listToSort.clear()
                    viewModel.listToSort.addAll(numbers)
                    viewModel.startSorting(delayMillis)
                    showTimeComplexity = false
                    showGraph = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Start Sort")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description Button
        Button(
            onClick = { showDescription = !showDescription },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF50d8ec)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showDescription) "Hide Description" else "Show Description")
        }

        // Description Text
        if (showDescription) {
            Text(
                text = "QuickSort is a divide-and-conquer algorithm that picks a pivot element and partitions the array into two sub-arrays: elements less than the pivot and elements greater than the pivot. It then recursively sorts the sub-arrays.",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Time Complexity Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { showGraph = !showGraph },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFc0f4b8)),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text(if (showGraph) "Hide Time Complexity" else "Show Time Complexity")
            }

            Button(
                onClick = { showSpaceComplexity = !showSpaceComplexity },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFfeffbe)),
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text(if (showSpaceComplexity) "Hide Space Complexity" else "Show Space Complexity")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Time Complexity Graphs
        if (showGraph) {
            Text(
                text = "Worst-Case Time Complexity: O(n^2)",
                color = Color.Red,
                fontSize = 18.sp
            )

            QuickTimeComplexityGraph(inputSize = worstCaseInputSize, complexityType = "Worst")
            Slider(
                value = worstCaseInputSize.toFloat(),
                onValueChange = { newValue -> worstCaseInputSize = newValue.toInt() }, // Independent state for worst-case
                valueRange = 1f..100f,
                steps = 99,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Best-Case Time Complexity: O(n log n)",
                color = Color.Green,
                fontSize = 18.sp
            )

            QuickTimeComplexityGraph(inputSize = bestCaseInputSize, complexityType = "Best")
            Slider(
                value = bestCaseInputSize.toFloat(),
                onValueChange = { newValue -> bestCaseInputSize = newValue.toInt() }, // Independent state for best-case
                valueRange = 1f..100f,
                steps = 99,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Display Space Complexity Graphs
        if (showSpaceComplexity) {
            // Worst-Case Space Complexity
            Text(
                text = "Worst-Case Space Complexity: O(n)",
                color = Color.Red,
                fontSize = 18.sp
            )

            QuickSpaceComplexityGraph(inputSize = spaceComplexityInputSizeWorst, complexityType = "Worst")
            Slider(
                value = spaceComplexityInputSizeWorst.toFloat(),
                onValueChange = { newValue -> spaceComplexityInputSizeWorst = newValue.toInt() }, // Independent state for Worst case
                valueRange = 1f..100f,
                steps = 99,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Best-Case Space Complexity
            Text(
                text = "Best-Case Space Complexity: O(log n)",
                color = Color.Green,
                fontSize = 18.sp
            )

            QuickSpaceComplexityGraph(inputSize = spaceComplexityInputSizeBest, complexityType = "Best")
            Slider(
                value = spaceComplexityInputSizeBest.toFloat(),
                onValueChange = { newValue -> spaceComplexityInputSizeBest = newValue.toInt() }, // Independent state for Best case
                valueRange = 1f..100f,
                steps = 99,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
// Time Complexity Graph
@Composable
fun QuickTimeComplexityGraph(inputSize: Int, complexityType: String) {
    val data = when (complexityType) {
        "Worst" -> QuickgenerateWorstCaseData(inputSize)
        "Best" -> QuickgenerateBestCaseData(inputSize)
        else -> emptyList()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val padding = 50.dp.toPx()
            val canvasWidth = size.width - padding * 2
            val canvasHeight = size.height - padding * 2

            // Draw axes
            drawLine(
                color = Color.White,
                start = Offset(padding, canvasHeight + padding),
                end = Offset(size.width - padding, canvasHeight + padding),
                strokeWidth = 4f
            )

            drawLine(
                color = Color.White,
                start = Offset(padding, padding),
                end = Offset(padding, canvasHeight + padding),
                strokeWidth = 4f
            )

            // Draw data points
            val maxY = data.maxOf { it.second }
            data.forEach { (x, y) ->
                drawCircle(
                    color = if (complexityType == "Worst") Color.Red else Color.Green,
                    center = Offset(
                        x * (canvasWidth / inputSize) + padding,
                        canvasHeight - (y * (canvasHeight / maxY)) + padding
                    ),
                    radius = 5f
                )
            }
        }
    }
}

fun QuickgenerateWorstCaseData(size: Int): List<Pair<Float, Float>> {
    return List(size) {
        val x = it.toFloat()
        val y = x * x  // Quadratic growth for worst-case O(n^2)
        x to y
    }
}

fun QuickgenerateBestCaseData(size: Int): List<Pair<Float, Float>> {
    return List(size) {
        val x = it.toFloat()
        val y = x * kotlin.math.log2(x + 1)  // Logarithmic growth for best-case O(n log n)
        x to y
    }
}

// Space Complexity Graph
@Composable
fun QuickSpaceComplexityGraph(inputSize: Int, complexityType: String) {
    val data = when (complexityType) {
        "Worst" -> generateWorstSpaceComplexityData(inputSize)
        "Best" -> generateBestSpaceComplexityData(inputSize)
        else -> emptyList()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val padding = 50.dp.toPx()
            val canvasWidth = size.width - padding * 2
            val canvasHeight = size.height - padding * 2

            // Draw axes
            drawLine(
                color = Color.White,
                start = Offset(padding, canvasHeight + padding),
                end = Offset(size.width - padding, canvasHeight + padding),
                strokeWidth = 4f
            )

            drawLine(
                color = Color.White,
                start = Offset(padding, padding),
                end = Offset(padding, canvasHeight + padding),
                strokeWidth = 4f
            )

            // Draw data points
            val maxY = data.maxOf { it.second }
            data.forEach { (x, y) ->
                drawCircle(
                    color = if (complexityType == "Worst") Color.Red else Color.Green,
                    center = Offset(
                        x * (canvasWidth / inputSize) + padding,
                        canvasHeight - (y * (canvasHeight / maxY)) + padding
                    ),
                    radius = 5f
                )
            }
        }
    }
}

fun generateWorstSpaceComplexityData(size: Int): List<Pair<Float, Float>> {
    return List(size) {
        val x = it.toFloat()
        val y = x  // Linear growth for worst-case O(n)
        x to y
    }
}

fun generateBestSpaceComplexityData(size: Int): List<Pair<Float, Float>> {
    return List(size) {
        val x = it.toFloat()
        val y = kotlin.math.log2(x + 1)  // Logarithmic growth for best-case O(log n)
        x to y
    }
}

