package com.example.appcomida

import android.graphics.Bitmap
import java.nio.ByteBuffer
import java.nio.ByteOrder

object Utils {
    // Função para pré-processar a imagem (mantida em Utils para evitar duplicação)
    fun preprocessImage(bitmap: Bitmap, imageSize: Int, channels: Int): ByteBuffer {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, true)
        val inputBuffer = ByteBuffer.allocateDirect(imageSize * imageSize * channels * 4) // 4 bytes por float
        inputBuffer.order(ByteOrder.nativeOrder())
        inputBuffer.rewind()

        // Inserir valores de cada pixel no buffer, normalizados entre 0 e 1
        for (y in 0 until imageSize) {
            for (x in 0 until imageSize) {
                val pixel = scaledBitmap.getPixel(x, y)
                val r = (pixel shr 16 and 0xFF) / 255.0f
                val g = (pixel shr 8 and 0xFF) / 255.0f
                val b = (pixel and 0xFF) / 255.0f
                inputBuffer.putFloat(r)
                inputBuffer.putFloat(g)
                inputBuffer.putFloat(b)
            }
        }

        return inputBuffer
    }

    // Função para processar os resultados da detecção
    fun parseDetectionResults(outputArray: Array<FloatArray>): List<DetectionResult> {
        val results = mutableListOf<DetectionResult>()

        // Suponha que outputArray[0] contém as pontuações para cada categoria
        for (i in outputArray[0].indices) {
            val confidence = outputArray[0][i]
            if (confidence > 0.5) { // Ajuste o limiar de confiança conforme necessário
                results.add(DetectionResult("Categoria $i", confidence))
            }
        }

        return results
    }
}
