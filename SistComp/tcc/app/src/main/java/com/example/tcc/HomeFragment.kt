package com.example.tcc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.tcc.controller.getClienteNome

import com.example.tcc.controller.getQuantidade
import com.example.tcc.controller.getprox
import com.example.tcc.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: ClienteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        userViewModel = ViewModelProvider(requireActivity()).get(ClienteViewModel::class.java)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val clienteId = userViewModel.clienteId.value
        if (clienteId != null) {
            lifecycleScope.launch {
                val nomeclie = getClienteNome(clienteId).toString()
                println(getClienteNome(clienteId))
                if (nomeclie != null) {
                    binding.textView.text = "Olá $nomeclie, como podemos te ajudar hoje?"
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                val quantidade = withContext(Dispatchers.IO) {
                    getQuantidade(clienteId)
                }
                println(quantidade)
                binding.textView2.text = quantidade.toString()
            }

            CoroutineScope(Dispatchers.Main).launch {
                val alimentoEstocado = withContext(Dispatchers.IO) {
                    getprox(clienteId)
                }
                if (alimentoEstocado != null) {
                    val formattedText = """
            Próximo Alimento a Vencer:
            Nome: ${alimentoEstocado.nomeAlimento}
            Data que vai Vencer: ${alimentoEstocado.validade}
        """.trimIndent()
                    binding.textView3.text = formattedText
                } else {
                    binding.textView3.text = "Erro ao obter o alimento"
                }
            }





        }
    }
}
