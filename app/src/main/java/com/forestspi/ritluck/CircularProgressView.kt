
package com.forestspi.ritluck

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class CircularProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var progress: Float = 0f
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 8f
        isAntiAlias = true
        color = ContextCompat.getColor(context, android.R.color.white)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()
        val radius = Math.min(width, height) / 2 - paint.strokeWidth / 2
        val centerX = width / 2
        val centerY = height / 2
        canvas.drawCircle(centerX, centerY, radius, paint)
        canvas.drawArc(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius,
            -90f,
            progress,
            false,
            paint
        )
    }

    fun setProgress(value: Float) {
        progress = value
        invalidate()
    }
}
