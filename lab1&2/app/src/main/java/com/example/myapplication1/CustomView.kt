package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    var isAlternativeMode: Boolean = false

    private val redPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private val bluePaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isAlternativeMode == false) {
            canvas.drawCircle(200f, 200f, 100f, redPaint)
        }
        else {
            canvas.drawRect(100f, 400f, 400f, 600f, bluePaint)
        }
    }
}