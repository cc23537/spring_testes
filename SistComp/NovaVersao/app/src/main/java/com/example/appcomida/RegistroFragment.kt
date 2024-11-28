package com.example.appcomida

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.appcomida.api.registrarCliente
import com.example.appcomida.databinding.FragmentRegistroBinding
import com.example.appcomida.dataclass.Cliente
import com.example.appcomida.rotas.ClienteRotas
import getRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.io.IOException
class RegistroFragment : Fragment() {

    private lateinit var binding: FragmentRegistroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {

            val nome = binding.edtNome.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassworld.text.toString()
            val altura = null
            val peso = null

            lifecycleScope.launch {
                try {
                    // Certifique-se de usar o Retrofit configurado para registrar o cliente
                    val response = registrarCliente(nome, email, password, altura, peso)
                    if (response.isSuccessful) {
                        findNavController().navigate(R.id.action_registroFragment_to_loginFragment)
                        Toast.makeText(requireContext(), "Registro feito com sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Erro ao registrar, tente novamente", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Erro: Não foi possível fazer o registro", Toast.LENGTH_SHORT).show()
                    println(e.localizedMessage)
                }
            }
        }

        binding.btncancel.setOnClickListener {
            findNavController().navigate(R.id.action_registroFragment_to_loginFragment)
        }
    }

    // Função suspend para registrar o cliente
    private suspend fun registrarCliente(
        nome: String,
        email: String,
        senha: String,
        altura: Double?,
        peso: Double?
    ): Response<Cliente> {
        return withContext(Dispatchers.IO) {
            val retrofit = getRetrofit() // Seu método Retrofit
            val service = retrofit.create(ClienteRotas::class.java)
            val cliente = Cliente(
                nomeCliente = nome,
                email = email,
                senha = senha,
                altura = altura,
                peso = peso
            )
            service.register(cliente)  // Chama o método de registro
        }
    }
}


