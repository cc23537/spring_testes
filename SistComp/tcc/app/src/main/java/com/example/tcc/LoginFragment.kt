package com.example.tcc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tcc.api.Rotas
import com.example.tcc.api.getRetrofit
import com.example.tcc.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        (activity as MainActivity).setMenuButtonVisibility(false)

        binding.btnLogin.setOnClickListener {
            binding.btnLogin.setOnClickListener {
                val email = binding.edtEmail.text.toString()
                val senha = binding.edtPassworld.text.toString()
                login(email, senha)
            }

            activity?.let {
                val preferences = it.getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.apply()
            }
        }

        binding.btnReg.setOnClickListener{
            (activity as MainActivity).replaceFragment(RegistroFragment(), false)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun login(email: String, senha: String){
        lifecycleScope.launch {
            try{
                val service = getRetrofit().create(Rotas::class.java)
                val response = service.loginC(email,senha)
                if(response.isSuccessful){
                    (activity as MainActivity).replaceFragment(HomeFragment(), true)
                }
            }
            catch (e: Exception){
                println(e)
            }
        }
    }
}
