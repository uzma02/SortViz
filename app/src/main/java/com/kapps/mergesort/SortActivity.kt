package com.kapps.mergesort

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kapps.mergesort.InsertionSortActivity
import com.kapps.mergesort.ui.theme.QuickSortTheme
import com.kapps.mergesort.ui.theme.gray
import com.kapps.mergesort.ui.theme.gray2
import com.kapps.mergesort.ui.theme.lightred
import com.kapps.mergesort.ui.theme.magenta
import com.kapps.mergesort.ui.theme.orange

class SortActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = magenta.toArgb()
        window.navigationBarColor = magenta.toArgb()

        setContent {
            QuickSortTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gray2)
                        .padding(40.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Choose your algorithm!",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE7E6E6),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            val intent = Intent(this@SortActivity, MergeSortActivity::class.java)
                            startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = magenta,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(
                            "Merge Sort",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = {
                            val intent = Intent(this@SortActivity, BubbleSortActivity::class.java)
                            startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = lightred,
                            contentColor = Color(0xFFC32F69)
                        ),
                        modifier = Modifier.fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(
                            "Bubble Sort",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = {
                            val intent = Intent(this@SortActivity, QuickSortActivity::class.java)
                            startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = magenta,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(
                            "Quick Sort",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = {
                            val intent = Intent(this@SortActivity, InsertionSortActivity::class.java)
                            startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = lightred,
                            contentColor = Color(0xFFC32F69)
                        ),
                        modifier = Modifier.fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(
                            "Insertion Sort",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = {
                            val intent = Intent(this@SortActivity, SelectionSortActivity::class.java)
                            startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = magenta,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(
                            "Selection Sort",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            }
        }
    }
}
