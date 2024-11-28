package com.example.appcomida.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appcomida.R
import com.example.appcomida.SharedViewModel
import com.example.appcomida.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: SharedViewModel

    // Variáveis para armazenar os argumentos recebidos
    private var email: String? = null
    private var senha: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Recupera os argumentos passados de LoginFragment
        arguments?.let {
            email = it.getString("email")
            senha = it.getString("senha")
        }

        // Exibe o email ou outro comportamento necessário
        println("Email recebido: $email")
        println("Senha recebida: $senha")

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

