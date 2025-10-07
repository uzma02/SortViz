package com.kapps.mergesort

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.kapps.mergesort.presentation.BubbleSortViewPseudo
import com.kapps.mergesort.presentation.MergeSortViewModel
import com.kapps.mergesort.presentation.MergeSortViewPseudo
import com.kapps.mergesort.presentation.state.BubbleListUiItem
import kotlin.random.Random


class BubbleSortActivity : ComponentActivity() {

    private val sortViewModel = SortViewModel()


    //@RequiresApi(Build.VERSION_CODES.N)
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickSortTheme {
                window.statusBarColor = orange.toArgb()
                window.navigationBarColor = orange.toArgb()

                val sortViewModel: SortViewModel = viewModel()
                val bubbleSortViewPseudo: BubbleSortViewPseudo = viewModel()
                val currentPseudocodeStep by bubbleSortViewPseudo.currentPseudocodeStep

                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gray)
                        .padding(20.dp)
                        .verticalScroll(scrollState)
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
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Magenta)
                    ) {
                        Text(
                            "Enter",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }

                    Button(onClick = {
                        //sortViewModel.startSorting()
                        //sortViewModel.listToSort =
                        //inputText.split(",").map { it.toInt() }.toMutableList()



                        sortViewModel.startSorting()
                        //bubbleSortViewPseudo.startSortingPseudo()
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Magenta)
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

                    // Pseudocode Visualization
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.6f)
                            .padding(5.dp)
                    ) {
                        BubbleSortPseudocode(currentStep = sortViewModel.currentStep)
                    }
                    var isButtonClicked by remember { mutableStateOf(false) }
                    var inputSize by remember { mutableStateOf(10) }
                    var inputSizeBC by remember { mutableStateOf(10) }

                    var isButtonClickedSC by remember { mutableStateOf(false) }
                    var inputSizeSC by remember { mutableStateOf(10) }


                    // Description Button
                    var showDescription by remember { mutableStateOf(false) }
                    var isButtonClickedDesc by remember { mutableStateOf(false) }

                    Button(
                        onClick = {
                            isButtonClickedDesc = !isButtonClickedDesc
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF50d8ec),
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Text(
                            "Description",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Description Text
                    if (showDescription) {
                        Text(
                            text = "Bubble sort is a simple sorting algorithm that repeatedly steps through the list, compares adjacent elements, and swaps them if they are in the wrong order. This process is repeated until the list is sorted. It is a stable sorting algorithm, meaning that elements with equal values maintain their relative order in the sorted output.",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    //Time Complexity Graphs
                    Button(
                        onClick = {
                            isButtonClicked = !isButtonClicked
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFc0f4b8),
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            "Time Complexity Graph",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (isButtonClicked) {
                        Text(
                            text = "Worst-Case Time Complexity: O(n^2)",
                            color = Color.Red,
                            fontSize = 18.sp
                        )
                        BubbleSortGraph(
                            generateBubbleSortLineData = { size -> generateBubbleSortLineData(size) },
                            generateTheoreticalBubbleSortLineData = { size -> generateTheoreticalBubbleSortLineData(size)},
                            description = Description().apply {
                                text = "Bubble Sort Time Complexity(O(n^2)"
                            },
                            inputSize = inputSize
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        // Display the current value of the slider
                        Text(text = "Input Size: $inputSize")
                        Slider(
                            value = inputSize.toFloat(),
                            onValueChange = { newValue ->
                                inputSize = newValue.toInt()
                            },
                            valueRange = 1f..100f,
                            steps = 99,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "Best-Case Time Complexity: O(n)",
                            color = Color.Red,
                            fontSize = 18.sp
                        )

                        BestCaseGraph(
                            generateBCLineData = { size -> generateBCLineData(size) },
                            generateTheoreticalBCLineData = { size -> generateTheoreticalBCLineData(size)},
                            description = Description().apply {
                                text = "Bubble Sort Time Complexity(O(n))"
                            },
                            inputSizeBC = inputSizeBC
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        // Display the current value of the slider
                        Text(text = "Input Size: $inputSizeBC")
                        Slider(
                            value = inputSizeBC.toFloat(),
                            onValueChange = { newValue ->
                                inputSizeBC = newValue.toInt()
                            },
                            valueRange = 1f..100f,
                            steps = 99,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    //Space Complexity Graphs

                    Button(
                        onClick = {
                            isButtonClickedSC = !isButtonClickedSC
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFfeffbe),
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            "Space Complexity Graph",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (isButtonClickedSC) {
                        SpaceComplexity(
                            generateSpaceComplexity = { size -> generateSpaceComplexity(size) },
                            generateTheoreticalSpaceComplexity = { size -> generateTheoreticalSpaceComplexity(size)},
                            description = Description().apply {
                                text = "Bubble Sort Space Complexity"
                            },
                            inputSizeSC = inputSizeSC
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        // Display the current value of the slider
                        Text(text = "Input Size: $inputSize")
                        Slider(
                            value = inputSizeSC.toFloat(),
                            onValueChange = { newValue ->
                                inputSizeSC = newValue.toInt()
                            },
                            valueRange = 1f..100f,
                            steps = 99,
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
    fun BubbleSortGraph(
        generateBubbleSortLineData: (Int) -> LineData,
        generateTheoreticalBubbleSortLineData: (Int) -> LineData,
        description: Description,
        inputSize: Int
    ) {
        LineChartView(
            context = LocalContext.current,
            configureChart = {
                val theoreticalLineData = generateTheoreticalBubbleSortLineData(inputSize)
                val randomLineData = generateBubbleSortLineData(inputSize)

                data = LineData().apply {
                    addDataSet(theoreticalLineData.getDataSetByIndex(0))
                    addDataSet(randomLineData.getDataSetByIndex(0))
                }
                this.description = description
                invalidate()
            },
            modifier = Modifier.height(300.dp)
        )
    }

    @Composable
    fun BestCaseGraph(
        generateBCLineData: (Int) -> LineData,
        generateTheoreticalBCLineData: (Int) -> LineData,
        description: Description,
        inputSizeBC: Int
    ) {
        LineChartView(
            context = LocalContext.current,
            configureChart = {
                val theoreticalLineData = generateTheoreticalBCLineData(inputSizeBC)
                val randomLineData = generateBCLineData(inputSizeBC)

                data = LineData().apply {
                    addDataSet(theoreticalLineData.getDataSetByIndex(0))
                    addDataSet(randomLineData.getDataSetByIndex(0))
                }
                this.description = description
                invalidate()
            },
            modifier = Modifier.height(300.dp)
        )
    }

    @Composable
    fun SpaceComplexity(
        generateSpaceComplexity: (Int) -> LineData,
        generateTheoreticalSpaceComplexity: (Int) -> LineData,
        description: Description,
        inputSizeSC: Int
    ) {
        LineChartView(
            context = LocalContext.current,
            configureChart = {
                val theoreticalLineData = generateTheoreticalSpaceComplexity(inputSizeSC)
                val randomLineData = generateSpaceComplexity(inputSizeSC)

                data = LineData().apply {
                    addDataSet(theoreticalLineData.getDataSetByIndex(0))
                    addDataSet(randomLineData.getDataSetByIndex(0))
                }
                this.description = description
                invalidate()
            },
            modifier = Modifier.height(300.dp)
        )
    }

    @Composable
    fun BubbleSortPseudocode(currentStep: Int) {
        val pseudocode = listOf(
            "BubbleSort(arr):",
            "    for i = 0 to n-1:",
            "        for j = i+1 to n:",
            "            if arr[j] > arr[j+1]:",
            "            swap(arr[j], arr[j+1])",
            "end"
        )


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

        Column(
            modifier = Modifier
                .fillMaxSize()
                //.background(Color.Gray, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            for (line in highlightedPseudocode) {
                Text(text = line, fontSize = 14.sp, color = Color.White)
            }
        }
    }


    private fun generateBubbleSortLineData(size: Int): LineData {
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

    private fun generateTheoreticalBubbleSortLineData(size: Int): LineData {
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


    private fun generateBCLineData(size: Int): LineData {
        val entries = List(size) {
            val x = (it + 1).toFloat()
            val y = (x  + Random.nextFloat() * 0.1 * x ).toFloat()
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

    private fun generateTheoreticalBCLineData(size: Int): LineData {
        val entries = List(size) {
            val x = (it + 1).toFloat()
            Entry(x, x )
        }
        val dataSet = LineDataSet(entries, "O(n)").apply {
            color = Color.Red.toArgb() // Line color
            valueTextColor = Color.Red.toArgb()
            setCircleColor(Color.Yellow.toArgb()) // Plot points color
            circleRadius = 4f // Plot points radius
        }
        return LineData(dataSet)
    }

    // This method generates a set of empirical space complexity points for Bubble Sort
    private fun generateSpaceComplexity(size: Int): LineData {
        val entries = List(size) {
            val x = (it + 1).toFloat()
            // Space complexity for Bubble Sort is O(1), so we keep y close to 1 with minimal variation
            val y = 1f + Random.nextFloat()*0.00000000001f // Reducing randomness to 0.01 for smaller variations
            Entry(x, y)
        }
        val dataSet = LineDataSet(entries, "Practical").apply {
            color = Color.Blue.toArgb() // Line color
            valueTextColor = Color.Blue.toArgb()
            setCircleColor(Color.Green.toArgb()) // Plot points color
            circleRadius = 4f // Plot points radius
        }
        return LineData(dataSet)
    }

    // This method generates the theoretical O(1) space complexity for Bubble Sort
    private fun generateTheoreticalSpaceComplexity(size: Int): LineData {
        val entries = List(size) {
            val x = (it + 1).toFloat()
            // Theoretical space complexity for Bubble Sort is O(1), constant for all inputs
            Entry(x, 1f) // Constant space complexity
        }
        val dataSet = LineDataSet(entries, "Theoretical").apply {
            color = Color.Red.toArgb() // Line color
            valueTextColor = Color.Red.toArgb()
            setCircleColor(Color.Yellow.toArgb()) // Plot points color
            circleRadius = 4f // Plot points radius
        }
        return LineData(dataSet)
    }



    @Composable
    fun TimeComplexityGraph(inputSize: Int, complexityType: String) {
        val data = when (complexityType) {
            "Worst" -> generateWorstCaseData(inputSize)
            "Best" -> generateBestCaseData(inputSize)
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

    fun generateWorstCaseData(size: Int): List<Pair<Float, Float>> {
        return List(size) {
            val x = (it + 1).toFloat()
            val y = x * x
            x to y
        }
    }

    fun generateBestCaseData(size: Int): List<Pair<Float, Float>> {
        return List(size) {
            val x = (it + 1).toFloat()
            val y = x
            x to y
        }
    }

    @Composable
    fun SpaceComplexityGraph(inputSize: Int, complexityType: String) {
        val data = when (complexityType) {
            "Worst" -> generateWorstSpaceData(inputSize)
            "Best" -> generateBestSpaceData(inputSize)
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

    fun generateWorstSpaceData(size: Int): List<Pair<Float, Float>> {
        return List(size) {
            val x = (it + 1).toFloat()
            val y = x
            x to y
        }
    }

    fun generateBestSpaceData(size: Int): List<Pair<Float, Float>> {
        return listOf(Pair(0f, 1f), Pair(size.toFloat(), 1f))
    }



}
