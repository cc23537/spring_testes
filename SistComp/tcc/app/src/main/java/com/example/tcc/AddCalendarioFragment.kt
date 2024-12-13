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
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.tcc.controller.registrarAlimentoEstocado
import com.example.tcc.databinding.FragmentAddCalendarioBinding
import com.example.tcc.databinding.FragmentCalendarioBinding
import com.example.tcc.dataclass.Alimento
import com.example.tcc.dataclass.AlimentoEstocado
import com.example.tcc.dataclass.AlimentoGDTO
import com.example.tcc.dataclass.ClientePft
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddCalendarioFragment : DialogFragment() {

    private var _binding: FragmentAddCalendarioBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: ClienteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = ViewModelProvider(requireActivity()).get(ClienteViewModel::class.java)
        _binding = FragmentAddCalendarioBinding.inflate(inflater, container, false)
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
        val idCliente =  userViewModel.clienteId.value?.toString()

        binding.btnFechar.setOnClickListener{
            dismiss()
        }

        binding.btnAdicionarAliemto.setOnClickListener {
            lifecycleScope.launch {
                val nomeAlimento = binding.edtNomeAddAlimento.text.toString()
                val calorias = binding.edtCalorias.text.toString()
                val caloriasDouble = calorias.toDoubleOrNull() ?: 0.0
                val especificacoes = binding.edtEspecifi.text.toString()
                val validade = binding.edtValidade.text.toString()
                val id = idCliente?.toIntOrNull() ?: 0

                try {
                    val formattedDate = formatDateToISO(validade) ?: throw IllegalArgumentException("Data inválida")
                    val alimento = AlimentoGDTO(nomeAlimento, caloriasDouble)
                    val clientePft = ClientePft(id)
                    val alimentoEstocado = AlimentoEstocado(
                        alimentoId = null,
                        alimentoASerEstocado = alimento,
                        especificacoes = especificacoes,
                        validade = LocalDate.parse(formattedDate),
                        quantidadeEstoque = 10, // Ajuste para o valor correto
                        cliente = clientePft
                    )
                    println(registrarAlimentoEstocado(alimentoEstocado))
                    registrarAlimentoEstocado(alimentoEstocado)
                    dismiss()
                } catch (e: Exception) {
                    println("Erro ao adicionar alimento: ${e.message}")
                }
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateToISO(dateString: String): String? {
        return try {
            val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val outputFormatter = DateTimeFormatter.ISO_LOCAL_DATE
            val date = LocalDate.parse(dateString, inputFormatter)
            val formattedDate = date.format(outputFormatter)
            println("Data formatada: $formattedDate") // Log da data formatada
            formattedDate
        } catch (e: Exception) {
            println("Erro ao formatar data: ${e.message}") // Log do erro
            null
        }
    }


}

