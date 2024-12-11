package com.example.tcc

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.tcc.databinding.FragmentAddListaDialogBinding
import com.example.tcc.dataclass.AlimentoASerComprado
import com.example.tcc.controller.registrarCompra
import com.example.tcc.dataclass.AlimentoASerComprado20
import com.example.tcc.dataclass.ClientePft
import kotlinx.coroutines.launch

class AddListaDialogFragment : DialogFragment() {
    private lateinit var userViewModel: ClienteViewModel
    private var _binding: FragmentAddListaDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvAdapter: RvLista

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddListaDialogBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(requireActivity()).get(ClienteViewModel::class.java)
        return _binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idCliente = userViewModel.clienteId.value
        isCancelable = true

        binding.btnAddC.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val nomeAlimento = binding.edtDescCompra.text.toString()
                    val quantidade = binding.edtQnt.text.toString().toInt()

                    if (idCliente != null) {
                        val alimentoComprado = AlimentoASerComprado20(nomeAlimento)
                        val cid = ClientePft(idCliente)
                        registrarCompra(alimentoComprado, quantidade, cid)
                        parentFragmentManager.setFragmentResult("addAlimentoRequest", Bundle())
                        dismiss()
                    } else {
                        println("Id do cliente não encontrado")
                    }
                } catch (e: Exception) {
                    println("Erro ao registrar compra: ${e.message}")
                }
            }
        }

        binding.btnFechar.setOnClickListener {
            println("Button Fechar clicked")
            dismiss()
        }
    }
}
