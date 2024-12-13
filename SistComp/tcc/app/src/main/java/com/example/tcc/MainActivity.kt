package com.example.tcc

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.appcomida.AjudaFragment
import com.example.tcc.api.Rotas
import com.example.tcc.api.getRetrofit
import com.example.tcc.controller.getClienteNome
import com.example.tcc.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var clienteViewModel: ClienteViewModel
    private var nomeclie: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clienteViewModel = ViewModelProvider(this).get(ClienteViewModel::class.java)

        val isLoggedIn = checkUserLoggedIn()

        if (savedInstanceState == null) {
            if (isLoggedIn) {
                val clienteId = getSavedClienteId()
                if (clienteId != null) {
                    clienteViewModel.setClienteId(clienteId)
                    Handler(Looper.getMainLooper()).postDelayed(
                        { replaceFragment(HomeFragment(), true) },
                        3000
                    )
                } else {
                    replaceFragment(LoginFragment(), false)
                }
            } else {
                replaceFragment(SplashFragment(), false)
                Handler(Looper.getMainLooper()).postDelayed(
                    { replaceFragment(LoginFragment(), false) },
                    3000
                )
            }
        }

        binding.btnToggleMenu.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Ações para os itens do menu
        binding.btnHome.setOnClickListener {
            replaceFragment(HomeFragment(), true)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.btnGallery.setOnClickListener {
            replaceFragment(ListaFragment(), true)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.btnWelcome.setOnClickListener {
            replaceFragment(CalendarioFragment(), true)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.btnAjuda.setOnClickListener{
            replaceFragment(AjudaFragment(), true)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        binding.btnReceitas.setOnClickListener{
            replaceFragment(ReceitasFragment(), true)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        // Observe o clienteId e faça a chamada da API
        clienteViewModel.clienteId.observe(this, { clienteId ->
            if (clienteId != null) {
                lifecycleScope.launch {
                    nomeclie = getClienteNome(clienteId).toString()
                    println(getClienteNome(clienteId))
                    if (nomeclie != null) {
                        binding.tvUsername.text = nomeclie
                    }
                }
            }
        })
    }

    private fun checkUserLoggedIn(): Boolean {
        val sharedPreferences = this.getSharedPreferences("login_prefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    fun saveLoginState(isLoggedIn: Boolean) {
        val sharedPreferences = this.getSharedPreferences("login_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_logged_in", isLoggedIn)
        editor.apply()
    }

    fun saveClienteId(clienteId: Int) {
        val sharedPreferences = this.getSharedPreferences("login_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("cliente_id", clienteId)
        editor.apply()
    }

    private fun getSavedClienteId(): Int? {
        val sharedPreferences = this.getSharedPreferences("login_prefs", MODE_PRIVATE)
        return if (sharedPreferences.contains("cliente_id")) {
            sharedPreferences.getInt("cliente_id", -1)
        } else {
            null
        }
    }

    private fun logout() {
        saveLoginState(false)
        val sharedPreferences = this.getSharedPreferences("login_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("cliente_id")
        editor.apply()
        replaceFragment(LoginFragment(), false)
    }

    fun replaceFragment(fragment: Fragment, showMenuButton: Boolean) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_content, fragment)
        fragmentTransaction.commit()
        setMenuButtonVisibility(showMenuButton)
        updateMenuButtonText(fragment)
    }

    fun setMenuButtonVisibility(visible: Boolean) {
        binding.btnToggleMenu.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun updateMenuButtonText(fragment: Fragment) {
        val screenName = when (fragment) {
            is HomeFragment -> "Home"
            is ListaFragment -> "Gallery"
            is CalendarioFragment -> "Calendário"
            is AjudaFragment -> "IA"
            is ReceitasFragment -> "Receitas"
            else -> "Menu"
        }
        binding.btnToggleMenu.text = screenName
    }
}
