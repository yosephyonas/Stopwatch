package com.forestspi.ritluck

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import java.util.*

class ClockView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()
    private val calendar = Calendar.getInstance()
    private val tickPaint = Paint()
    private val clockHandPaint = Paint()
    private val secondHandPaint = Paint()

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.white)

        tickPaint.isAntiAlias = true
        tickPaint.style = Paint.Style.STROKE
        tickPaint.strokeWidth = 4f
        tickPaint.color = ContextCompat.getColor(context, R.color.black)

        clockHandPaint.isAntiAlias = true
        clockHandPaint.style = Paint.Style.STROKE
        clockHandPaint.strokeWidth = 8f
        clockHandPaint.color = ContextCompat.getColor(context, R.color.grey)

        secondHandPaint.isAntiAlias = true
        secondHandPaint.style = Paint.Style.STROKE
        secondHandPaint.strokeWidth = 4f
        secondHandPaint.color = ContextCompat.getColor(context, R.color.primary)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawClockFace(canvas)
        drawClockHands(canvas)
    }

    private fun drawClockFace(canvas: Canvas) {
        val width = width
        val height = height
        val radius = Math.min(width, height) / 2.5f

        // Draw clock background
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)

        // Draw tick marks
        for (i in 0 until 60) {
            val tickRadius = if (i % 5 == 0) radius * 0.9f else radius * 0.95f
            val angle = Math.toRadians((i * 6).toDouble())
            val startX = (width / 2 + radius * Math.cos(angle)).toFloat()
            val startY = (height / 2 + radius * Math.sin(angle)).toFloat()
            val endX = (width / 2 + tickRadius * Math.cos(angle)).toFloat()
            val endY = (height / 2 + tickRadius * Math.sin(angle)).toFloat()
            canvas.drawLine(startX, startY, endX, endY, tickPaint)
        }
    }

    private fun drawClockHands(canvas: Canvas) {
        val width = width
        val height = height
        val radius = Math.min(width, height) / 2.5f

        calendar.timeInMillis = System.currentTimeMillis()

        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        drawHand(canvas, hour / 12f * 360, radius * 0.5f, clockHandPaint)
        drawHand(canvas, minute / 60f * 360, radius * 0.7f, clockHandPaint)
        drawHand(canvas, second / 60f * 360, radius * 0.9f, secondHandPaint)
    }

    private fun drawHand(canvas: Canvas, angle: Float, length: Float, paint: Paint) {
        val width = width
        val height = height
        val centerX = width / 2f
        val centerY = height / 2f

        val endX = centerX + length * Math.cos(Math.toRadians(angle.toDouble() - 90)).toFloat()
        val endY = centerY + length * Math.sin(Math.toRadians(angle.toDouble() - 90)).toFloat()

        canvas.drawLine(centerX, centerY, endX, endY, paint)
    }
}
