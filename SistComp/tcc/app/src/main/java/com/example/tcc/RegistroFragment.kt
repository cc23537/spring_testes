package com.example.tcc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.tcc.api.Rotas
import com.example.tcc.api.getRetrofit
import com.example.tcc.controller.registrarCliente
import com.example.tcc.databinding.FragmentRegistroBinding
import kotlinx.coroutines.launch
import javax.net.ssl.SSLException


class RegistroFragment : Fragment() {

    private var _binding: FragmentRegistroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnLogin.setOnClickListener{

            val nome = binding.edtNome.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassworld.text.toString()
            val altura = null
            val peso = null
            val apiService = getRetrofit().create(Rotas::class.java)
            lifecycleScope.launch {
                try {

                    val response = registrarCliente(nome, email, password, altura, peso)
                    if (response != null){
                        (activity as MainActivity).replaceFragment(LoginFragment(), false)
                    }
                } catch (e: SSLException) {
                    Log.e("SSLException", "SSL handshake failed: ${e.message}")
                } catch (e: Exception) {
                    println("nao foi possivel fazer registro" + e)
                }

            }
        }
        binding.btncancel.setOnClickListener{

            (activity as MainActivity).replaceFragment(LoginFragment(), false)
        }
        return view
    }
}