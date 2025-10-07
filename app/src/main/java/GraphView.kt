package com.kapps.mergesort

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.hypot
import kotlin.random.Random

class GraphView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    val nodes = mutableListOf<Node>()
    val edges = mutableListOf<Edge>()
    private val nodePaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }
    val edgePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
    }
    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }
    private val edgeTextPaint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }

    var startNode: Node? = null

    init {
        // Initialize some sample nodes
        nodes.add(Node(100f, 100f, "0"))
        nodes.add(Node(300f, 100f, "1"))
        nodes.add(Node(500f, 100f, "2"))
        nodes.add(Node(200f, 300f, "3"))
        nodes.add(Node(400f, 300f, "4"))
        nodes.add(Node(600f, 300f, "5"))

        // Initialize edges with random weights
        addEdge(0, 1)
        addEdge(1, 2)
        addEdge(1, 3)
        addEdge(2, 4)
        addEdge(3, 5)
    }

    private fun addEdge(startIndex: Int, endIndex: Int) {
        val weight = Random.nextInt(1, 10)
        edges.add(Edge(nodes[startIndex], nodes[endIndex], weight))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw edges
        for (edge in edges) {
            canvas.drawLine(edge.start.x, edge.start.y, edge.end.x, edge.end.y, edgePaint)
            // Draw the edge weight
            val midX = (edge.start.x + edge.end.x) / 2
            val midY = (edge.start.y + edge.end.y) / 2
            canvas.drawText(edge.weight.toString(), midX, midY, edgeTextPaint)
        }

        // Draw nodes
        for (node in nodes) {
            nodePaint.color = when {
                node == startNode -> Color.GREEN
                node.visited -> Color.RED
                else -> Color.BLUE
            }
            canvas.drawCircle(node.x, node.y, 50f, nodePaint)
            canvas.drawText(node.label, node.x, node.y + 15f, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            for (node in nodes) {
                if (hypot((event.x - node.x).toDouble(), (event.y - node.y).toDouble()) < 50) {
                    startNode = node
                    invalidate()
                    break
                }
            }
        }
        return super.onTouchEvent(event)
    }

    data class Node(
        val x: Float,
        val y: Float,
        val label: String,
        var distance: Int = Int.MAX_VALUE,
        var visited: Boolean = false,
        var predecessor: Node? = null
    )

    data class Edge(val start: Node, val end: Node, val weight: Int)
}
