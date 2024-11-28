package com.example.appcomida

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.appcomida.databinding.FragmentLoginBinding
import com.example.appcomida.rotas.ClienteRotas
import getRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response



class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Login button click
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val senha = binding.edtPassworld.text.toString()
            login(email, senha)

        }

        // Register button click
        binding.btnReg.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registroFragment)
        }
    }

    private fun login(email: String, senha: String) {
        lifecycleScope.launch {
            try {
                // Chama a API de login e obtém a resposta
                val response = getLoginResponse(email, senha)
                if (response.isSuccessful) {
                    // Salva as credenciais no SharedViewModel e UserViewModel
                    val idCliente = extractIdFromResponse(response)
                    sharedViewModel.setEmail(email)
                    userViewModel.setUserCredentials(email, senha, idCliente)

                    // Usando Safe Args para passar os argumentos
                    val action = LoginFragmentDirections
                        .actionLoginFragmentToMobileNavigation(email, senha)  // Passando os argumentos
                    findNavController().navigate(action)

                    // Salva as credenciais no SharedPreferences
                    saveEmail(email)
                    saveLogin()
                } else {
                    // Exibe erro se a resposta não for bem-sucedida
                    println("Erro na requisição: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Erro: ${e.localizedMessage}")
            }
        }
    }

    // Função suspend para chamar a API e obter a resposta
    private suspend fun getLoginResponse(email: String, senha: String): Response<String> {
        return withContext(Dispatchers.IO) {
            val service = getRetrofit().create(ClienteRotas::class.java)
            // Chama a função login na API
            service.login(email, senha)
        }
    }

    // Função para extrair o ID do cliente da resposta
    private fun extractIdFromResponse(response: Response<String>): Int {
        return response.body()?.toIntOrNull() ?: 0
    }

    // Função para salvar o email nas SharedPreferences
    private fun saveEmail(email: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("user_email", email).apply()
    }

    // Função para marcar que o usuário está logado nas SharedPreferences
    private fun saveLogin() {
        val sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
    }
}

