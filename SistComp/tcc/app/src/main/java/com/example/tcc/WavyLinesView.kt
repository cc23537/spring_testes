package com.example.tcc

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class WavyLinesView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paintBlue1: Paint = Paint().apply {
        color = 0xFF1E3A8A.toInt() // Azul escuro
        style = Paint.Style.FILL
    }

    private val paintBlue2: Paint = Paint().apply {
        color = 0xFF2563EB.toInt() // Azul médio
        style = Paint.Style.FILL
    }

    private val paintBlue3: Paint = Paint().apply {
        color = 0xFF3B82F6.toInt() // Azul claro
        style = Paint.Style.FILL
    }

    private val path1: Path = Path()
    private val path2: Path = Path()
    private val path3: Path = Path()

    private var animationProgress: Float = 0f

    init {
        startAnimation()
    }

    private fun startAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 4000 // Tempo mais longo para suavidade
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART

            addUpdateListener { animation ->
                animationProgress = animation.animatedValue as Float
                invalidate() // Redesenha a tela com o progresso atualizado
            }
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()
        val animatedWidth = width + (width * animationProgress)

        // Define os caminhos das linhas onduladas superiores
        path1.apply {
            reset()
            moveTo(-width + animatedWidth, 200f)
            cubicTo(
                width / 4 + animatedWidth, 150f,
                3 * width / 4 + animatedWidth, 250f,
                width + animatedWidth, 200f
            )
            lineTo(width, 0f)
            lineTo(-width, 0f)
            close()
        }

        path2.apply {
            reset()
            moveTo(-width + animatedWidth, 250f)
            cubicTo(
                width / 3 + animatedWidth, 300f,
                2 * width / 3 + animatedWidth, 200f,
                width + animatedWidth, 250f
            )
            lineTo(width, 0f)
            lineTo(-width, 0f)
            close()
        }

        path3.apply {
            reset()
            moveTo(-width + animatedWidth, 300f)
            cubicTo(
                width / 4 + animatedWidth, 350f,
                3 * width / 4 + animatedWidth, 270f,
                width + animatedWidth, 300f
            )
            lineTo(width, 0f)
            lineTo(-width, 0f)
            close()
        }

        // Desenha as linhas onduladas superiores
        canvas.drawPath(path1, paintBlue1)
        canvas.drawPath(path2, paintBlue2)
        canvas.drawPath(path3, paintBlue3)

        // Ajusta as linhas onduladas inferiores (para simetria, se necessário)
        path1.apply {
            reset()
            moveTo(-width + animatedWidth, height - 200f)
            cubicTo(
                width / 4 + animatedWidth, height - 250f,
                3 * width / 4 + animatedWidth, height - 150f,
                width + animatedWidth, height - 200f
            )
            lineTo(width, height)
            lineTo(-width, height)
            close()
        }

        path2.apply {
            reset()
            moveTo(-width + animatedWidth, height - 250f)
            cubicTo(
                width / 3 + animatedWidth, height - 300f,
                2 * width / 3 + animatedWidth, height - 200f,
                width + animatedWidth, height - 250f
            )
            lineTo(width, height)
            lineTo(-width, height)
            close()
        }

        path3.apply {
            reset()
            moveTo(-width + animatedWidth, height - 300f)
            cubicTo(
                width / 4 + animatedWidth, height - 350f,
                3 * width / 4 + animatedWidth, height - 270f,
                width + animatedWidth, height - 300f
            )
            lineTo(width, height)
            lineTo(-width, height)
            close()
        }

        // Desenha as linhas onduladas inferiores
        canvas.drawPath(path1, paintBlue1)
        canvas.drawPath(path2, paintBlue2)
        canvas.drawPath(path3, paintBlue3)
    }
}
