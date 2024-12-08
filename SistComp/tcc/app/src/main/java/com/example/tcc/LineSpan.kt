package com.example.appcomida.ui.Calendario

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan

class LineSpan(private val color: Int, private val thickness: Float, private val widthPercentage: Float) : LineBackgroundSpan {
    override fun drawBackground(
        canvas: Canvas,
        paint: Paint,
        left: Int,
        right: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        charSequence: CharSequence,
        start: Int,
        end: Int,
        lineNumber: Int
    ) {
        // Salve o estilo original do Paint
        val oldColor = paint.color
        val oldStyle = paint.style
        val oldStrokeWidth = paint.strokeWidth

        // Defina a cor, estilo e espessura da linha
        paint.color = color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = thickness

        // Calcule o novo comprimento da linha com base na porcentagem
        val lineWidth = (right - left) * widthPercentage / 100
        val startX = left + (right - left - lineWidth) / 2
        val endX = startX + lineWidth

        // Desenhe uma linha horizontal abaixo do texto do dia
        canvas.drawLine(startX, bottom.toFloat() + thickness, endX, bottom.toFloat() + thickness, paint)

        // Restaure o estilo original do Paint
        paint.color = oldColor
        paint.style = oldStyle
        paint.strokeWidth = oldStrokeWidth
    }
}