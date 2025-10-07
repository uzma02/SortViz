package com.kapps.mergesort

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kapps.mergesort.presentation.SortViewModel
import com.kapps.mergesort.ui.theme.QuickSortTheme
import com.kapps.mergesort.ui.theme.gray
import com.kapps.mergesort.ui.theme.orange
import androidx.compose.runtime.*
import androidx.compose.material.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.kapps.mergesort.presentation.SelectionSortViewModel
import kotlin.math.ln
import kotlin.random.Random


class SelectionSortActivity : ComponentActivity() {

    private val sortViewModel = SelectionSortViewModel()


    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickSortTheme {
                window.statusBarColor = orange.toArgb()
                window.navigationBarColor = orange.toArgb()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gray)
                        .padding(20.dp)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    // EditText field for user input
                    var inputText by remember { mutableStateOf("") }
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        label = { Text("Enter comma-separated numbers") },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White
                        ),

                        )
                    Button(onClick = {
                        // Pass the input text to the view model to initialize the list
                        sortViewModel.initializeListWithInput(inputText)
                    },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF00FF)),

                    ) {
                        Text(
                            "Enter",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }

                    Button(onClick = {
                        sortViewModel.startSorting()

                    }, shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF00FF))
                    ) {
                        Text(
                            "Sort List",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }

                    LazyRow(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    )
                    {
                        items(
                            sortViewModel.listToSort,
                            key = {
                                it.id
                            }
                        ){
                            val borderStroke = if(it.isCurrentlyCompared){
                                BorderStroke(width = 3.dp,Color.White,)
                            }else{
                                BorderStroke(width = 0.dp,Color.Transparent)
                            }
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(it.color, RoundedCornerShape(15.dp))
                                    .border(borderStroke, RoundedCornerShape(15.dp))
                                    .animateItemPlacement(
                                        tween(300)
                                    ),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    "${it.value}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp
                                )


                            }
                        }
                    }
                    SelectionSortPseudocode(currentStep = sortViewModel.currentStep)
                    var isDescriptionVisible by remember { mutableStateOf(false) }
                    var isButtonClicked by remember { mutableStateOf(false) }
                    var isSpaceButtonClicked by remember { mutableStateOf(false) }
                    var inputSize by remember { mutableStateOf(10) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                isDescriptionVisible = !isDescriptionVisible
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF50d8ec),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                "Description",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (isDescriptionVisible) {
                        SelectionSortDescription()
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Align buttons side by side
                    ) {
                        // Time Complexity Button
                        Button(
                            onClick = {
                                isButtonClicked = !isButtonClicked
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFc0f4b8),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(0.8f)
                        // Take up equal space
                        ) {
                            Text(
                                "Time Complexity",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Space Complexity Button
                        Button(
                            onClick = {
                                isSpaceButtonClicked = !isSpaceButtonClicked
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFfeffbe),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(0.8f) // Take up equal space
                        ) {
                            Text(
                                "Space Complexity",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

// Show Time Complexity Graph
                    if (isButtonClicked) {
                        Text(
                            "Time Complexity: O(n^2) \nUse slider for number of inputs and click button again to see results",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color= Color(0xFFFFFFFF)
                        )
                        SelectionSortGraph(
                            generateLineData = { size -> generateRandomLineData(size) },
                            generateTheoreticalLineData = { size -> generateTheoreticalLineData(size) },
                            inputSize = inputSize,
                            description= "Time Complexity O(n^2)"
                        )

                        Slider(
                            value = inputSize.toFloat(),
                            onValueChange = { newValue ->
                                inputSize = newValue.toInt()
                            },
                            valueRange = 1f..50f,
                            steps = 49,
                            modifier = Modifier.fillMaxWidth()
                        )

                    }

// Show Space Complexity Graph
                    if (isSpaceButtonClicked) {
                        Text(
                            "Space Complexity: O(1) \nUse slider for number of inputs and click button again to see results",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color= Color(0xFFFFFFFF)
                        )
                        SelectionSortGraph(
                            generateLineData = { size -> generateSpaceComplexityData(size) }, // New function for space complexity
                            generateTheoreticalLineData = { size -> generateTheoreticalSpaceComplexityData(size) }, // New function for theoretical space complexity

                            inputSize = inputSize,
                            description="Space Complexity O(1)"
                        )

                        Slider(
                            value = inputSize.toFloat(),
                            onValueChange = { newValue ->
                                inputSize = newValue.toInt()
                            },
                            valueRange = 1f..50f,
                            steps = 49,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
    @Composable
    fun LineChartView(
        context: Context,
        modifier: Modifier = Modifier,
        configureChart: LineChart.() -> Unit = {}
    ) {
        AndroidView(
            factory = { ctx ->
                LineChart(ctx).apply(configureChart)
            },
            modifier = modifier.fillMaxWidth()
        )
    }

    @Composable
    fun SelectionSortGraph(
        generateLineData: (Int) -> LineData,
        generateTheoreticalLineData: (Int) -> LineData,
        inputSize: Int,
        description: String
    ) {
        LineChartView(
            context = LocalContext.current,
            configureChart = {
                val theoreticalLineData = generateTheoreticalLineData(inputSize)
                val randomLineData = generateLineData(inputSize)

                data = LineData().apply {
                    addDataSet(theoreticalLineData.getDataSetByIndex(0))
                    addDataSet(randomLineData.getDataSetByIndex(0))
                }

                invalidate()
            },
            modifier = Modifier.height(300.dp)
        )
    }

    @Composable
    fun SelectionSortDescription() {
        val descriptionText = "Selection Sort repeatedly finds the smallest element from the unsorted part of the array and swaps it with the first unsorted element. It divides the array into sorted and unsorted parts, growing the sorted portion with each pass. \nSelection Sort is not stable, meaning it may not preserve the relative order of equal elements."

        Text(
            text = descriptionText,
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
    }

    @Composable
    fun SelectionSortPseudocode(currentStep: Int) {
        val pseudocode = listOf(
            "SelectionSort(arr):",
            "    for i = 0 to n-1:",
            "        minIndex = i",
            "        for j = i+1 to n:",
            "            if arr[j] < arr[minIndex]:",
            "                minIndex = j",
            "        if minIndex != i:",
            "            swap(arr[i], arr[minIndex])"
        )

        // Build the pseudocode with highlighted steps
        val highlightedPseudocode = pseudocode.mapIndexed { index, line ->
            if (index == currentStep) {
                AnnotatedString.Builder().apply {
                    withStyle(style = SpanStyle(color = Color.Yellow, fontWeight = FontWeight.Bold)) {
                        append(line)
                    }
                }.toAnnotatedString()
            } else {
                AnnotatedString(line)
            }
        }

        // Display the pseudocode
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(gray, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            for (line in highlightedPseudocode) {
                Text(text = line, fontSize = 16.sp, color = Color.White)
            }
        }
    }


    private fun generateRandomLineData(size: Int): LineData {
        val entries = List(size) {
            val x = (it + 1).toFloat()
            val y = (x * x + Random.nextFloat() * 0.2 * x * x).toFloat()
            Entry(x, y)
        }
        val dataSet = LineDataSet(entries, "Test Points").apply {
            color = Color.Blue.toArgb() // Line color
            valueTextColor = Color.Blue.toArgb()
            setCircleColor(Color.Green.toArgb()) // Plot points color
            circleRadius = 4f // Plot points radius
        }
        return LineData(dataSet)
    }

    private fun generateTheoreticalLineData(size: Int): LineData {
        val entries = List(size) {
            val x = (it + 1).toFloat()
            Entry(x, x * x)
        }
        val dataSet = LineDataSet(entries, "O(n^2)").apply {
            color = Color.Red.toArgb() // Line color
            valueTextColor = Color.Red.toArgb()
            setCircleColor(Color.Yellow.toArgb()) // Plot points color
            circleRadius = 4f // Plot points radius
        }
        return LineData(dataSet)
    }

    private fun generateSpaceComplexityData(size: Int): LineData {
        val entries = List(size) {
            val x = (it + 1).toFloat()
            val y = ( 1 + Random.nextFloat() * 0.2 * 1).toFloat()
            Entry(x, y)
        }
        val dataSet = LineDataSet(entries, "Test Points").apply {
            color = Color.Blue.toArgb() // Line color
            valueTextColor = Color.Blue.toArgb()
            setCircleColor(Color.Green.toArgb()) // Plot points color
            circleRadius = 4f // Plot points radius
        }
        return LineData(dataSet)
    }

    private fun generateTheoreticalSpaceComplexityData(size: Int): LineData {
        val entries = List(size) {
            val x = (it + 1).toFloat()
            Entry(x, 1F)
        }
        val dataSet = LineDataSet(entries, "O(1)").apply {
            color = Color.Red.toArgb() // Line color
            valueTextColor = Color.Red.toArgb()
            setCircleColor(Color.Yellow.toArgb()) // Plot points color
            circleRadius = 4f // Plot points radius
        }
        return LineData(dataSet)
    }

}
