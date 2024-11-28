package com.example.appcomida

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope

import com.example.appcomida.api.registrarAlimentoEstocado
import com.example.appcomida.api.registrarCliente
import com.example.appcomida.api.registrarCompra
import com.example.appcomida.databinding.FragmentAddAlimentosDialogBinding
import com.example.appcomida.dataclass.Alimento
import com.example.appcomida.dataclass.AlimentoEstocado
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class AddAlimentosDialogFragment : DialogFragment() {

    private var _binding: FragmentAddAlimentosDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

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
                val calorias = binding.edtCalorias.text.toString().toDouble()
                val especificacoes = binding.edtEspecifi.text.toString()
                val validade = binding.edtValidade.text.toString()
                val validadeDate = formatDateToISO(validade)
                userViewModel.idCliente.observe(viewLifecycleOwner, Observer { idCliente ->
                    if (idCliente != null && validadeDate != null) {
                        val alimentoEstocado = AlimentoEstocado(
                            alimentoASerEstocado = Alimento(
                                nomeAlimento = nomeAlimento,
                                calorias = calorias,
                                cliente = idCliente
                            ),
                            validade = validadeDate,
                            especificacoes = especificacoes
                        )
                        try {
                            registrarAlimentoEstocado(alimentoEstocado)
                        } catch (e: Exception) {
                            println(e)
                        }
                        parentFragmentManager.setFragmentResult("addAlimentoRequest", Bundle())
                        dismiss()
                    } else {
                        println("Id do cliente não encontrado ou data inválida")
                    }
                })
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateToISO(dateString: String): LocalDate? {
        return try {

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            LocalDate.parse(dateString, formatter)
        } catch (e: DateTimeParseException) {

            null
        }
    }

}