package com.example.tcc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.AlertDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.tcc.databinding.FragmentCalendarioBinding

class AddCalendarioFragment : DialogFragment() {

    private var _binding: FragmentAdd? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAlimentosDialogBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = true

        binding.btnFechar.setOnClickListener{
            dismiss()
        }

        binding.btnAdicionarAliemto.setOnClickListener {
            lifecycleScope.launch {
                val nomeAlimento = binding.edtNomeAddAlimento.text.toString()
                val calorias = binding.edtCalorias.text.toString()
                val especificacoes = binding.edtEspecifi.text.toString()
                val validade = binding.edtValidade.text.toString()

                try {
                    val formattedDate = formatDateToISO(validade) ?: "Invalid date"
                    registrarAlimento(nomeAlimento, calorias.toDouble(), especificacoes, formattedDate)
                } catch (e: Exception) {
                    println(e)
                }

                parentFragmentManager.setFragmentResult("addAlimentoRequest", Bundle())

                dismiss()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateToISO(dateString: String): String? {
        return try {
            // o input
            val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            // o formato
            val outputFormatter = DateTimeFormatter.ISO_LOCAL_DATE

            // str pra date
            val date = LocalDate.parse(dateString, inputFormatter)
            // formata
            date.format(outputFormatter)
        } catch (e: Exception) {

            null
        }
    }

}