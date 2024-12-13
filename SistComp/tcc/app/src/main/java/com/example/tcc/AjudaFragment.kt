package com.example.appcomida

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.createScaledBitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.tcc.databinding.FragmentAjudaBinding
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter
import java.io.FileNotFoundException
import java.time.LocalDateTime

class AjudaFragment : Fragment() {

    private var _binding: FragmentAjudaBinding? = null
    private val binding get() = _binding!!

    private val CAMERA_REQUEST_CODE = 100
    private val CAMERA_PERMISSION_CODE = 101

    private lateinit var fruitDetection: FruitDetection
    private lateinit var interpreter: Interpreter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAjudaBinding.inflate(inflater, container, false)

        fruitDetection = FruitDetection(requireContext())
        interpreter = fruitDetection.loadModel()

        binding.btnAbrirCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                abrirCamera()
            } else {
                Toast.makeText(context, "Solicitando permissão da câmera", Toast.LENGTH_SHORT).show()
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            }
        }
        binding.btnAdd.setOnClickListener{
            val add = AddFromIa()
            add.show(parentFragmentManager, "AddDialog")
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun abrirCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }
    private fun loadFruitLabels(): List<String> {
        val assetManager = context?.assets
        val labels = mutableListOf<String>()

        try {
            if (assetManager != null) {
                assetManager.open("labels.txt").bufferedReader().useLines { lines ->
                    lines.forEach { line ->
                        val parts = line.split(" ")
                        if (parts.size > 1) {
                            labels.add(parts.subList(1, parts.size).joinToString(" ")) // Ignorar o índice
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw FileNotFoundException("Arquivo labels.txt não encontrado no diretório assets.")
        }
        return labels
    }

    private fun loadFruitDurations(): List<Long> {
        val assetManager = context?.assets
        val durations = mutableListOf<Long>()

        try {
            if (assetManager != null) {
                assetManager.open("datadevalidade.txt").bufferedReader().useLines { lines ->
                    lines.forEach { line ->
                        durations.add(line.trim().toLongOrNull() ?: 0L) // Adiciona 0 se não puder converter
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw FileNotFoundException("Arquivo datadevalidade.txt não encontrado no diretório assets.")
        }
        return durations
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            binding.imageView.setImageBitmap(photo)

            val formattedPhoto = formatarImagem(photo, 224, 224)

            val detectionResults = fruitDetection.detectFruit(formattedPhoto, interpreter)
            binding.resultTextView.text = detectionResults.joinToString("\n") { "${it.label}: ${it.confidence}" }

            // Carregar rótulos e durações
            val fruitLabels = loadFruitLabels()
            val fruitDurations = loadFruitDurations()

            detectionResults.forEach { result ->
                val label = result.label
                val confidence = result.confidence

                if (confidence > 0.0f) {
                    val index = fruitLabels.indexOf(label)
                    val weeks = if (index in fruitDurations.indices) fruitDurations[index] else null

                    // Calcular a data de validade apenas se weeks for válido
                    val validade = weeks?.let {
                        if (it > 0) {
                            LocalDateTime.now().plusWeeks(it)
                        } else {
                            null
                        }
                    }

                    // Log para verificar validade
                    println("Validade calculada: $validade")

                    // Registrar o alimento apenas se a validade for válida
                    validade?.let {
                        lifecycleScope.launch {
                            //registrarAlimento(label, null, null, it.toString())
                        }
                    } ?: run {
                        println("Validade inválida para $label")
                    }
                }
            }
        }
    }

    private fun formatarImagem(bitmap: Bitmap, largura: Int, altura: Int): Bitmap {
        // Redimensiona a imagem para as dimensões necessárias pelo modelo
        return createScaledBitmap(bitmap, largura, altura, true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permissão da câmera concedida", Toast.LENGTH_SHORT).show()
                abrirCamera()
            } else {
                Toast.makeText(context, "Permissão da câmera negada", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

