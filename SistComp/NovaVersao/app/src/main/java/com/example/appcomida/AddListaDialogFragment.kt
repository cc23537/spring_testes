package com.example.appcomida

import RvLista
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.appcomida.api.registrarAlimentoEstocado
import com.example.appcomida.api.registrarCompra
import com.example.appcomida.databinding.FragmentAddListaDialogBinding
import com.example.appcomida.dataclass.Alimento
import com.example.appcomida.dataclass.AlimentoEstocado
import com.example.appcomida.dataclass.Compra

import kotlinx.coroutines.launch


class AddListaDialogFragment : DialogFragment() {
    private lateinit var userViewModel: UserViewModel
    private var _binding: FragmentAddListaDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvAdapter: RvLista

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddListaDialogBinding.inflate(inflater, container, false)
        return _binding!!.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = true

        binding.btnAddC.setOnClickListener {
            lifecycleScope.launch {
                val nomeAlimento = binding.txtNomeAddAlimento.text.toString()
                val quantidade = binding.edtQnt.text.toString()
                userViewModel.idCliente.observe(viewLifecycleOwner, Observer { idCliente ->
                    if (idCliente != null) {
                        val alimentoComprado = Alimento(
                           nomeAlimento = nomeAlimento,
                            cliente = idCliente
                        )
                        try {
                            registrarCompra(alimentoComprado,quantidade.toInt(),idCliente)
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

        binding.btnFechar.setOnClickListener{
            dismiss()
        }

    }



}