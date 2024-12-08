package com.example.tcc

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.tcc.api.Rotas
import com.example.tcc.api.getRetrofit
import com.example.tcc.controller.getClienteNome
import com.example.tcc.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var clienteViewModel: ClienteViewModel
    private var nomeclie: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clienteViewModel = ViewModelProvider(this).get(ClienteViewModel::class.java)


        val isLoggedIn = checkUserLoggedIn()

        if (savedInstanceState == null) {
            if (isLoggedIn) {
                replaceFragment(HomeFragment(), true)
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

        // Observe o clienteId e faça a chamada da API
        clienteViewModel.clienteId.observe(this, { clienteId ->
            if (clienteId != null) {
                lifecycleScope.launch {
                        nomeclie = getClienteNome(clienteId).toString()
                    println(getClienteNome(clienteId))
                    if (nomeclie != null){
                        binding.tvUsername.text = nomeclie
                    }

                }
            }
        })
    }

    private fun checkUserLoggedIn(): Boolean {
        // Adicione sua lógica de verificação de login aqui
        return false
    }

    fun replaceFragment(fragment: Fragment, showMenuButton: Boolean) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_content, fragment)
        fragmentTransaction.commit()
        setMenuButtonVisibility(showMenuButton)
    }

    fun setMenuButtonVisibility(visible: Boolean) {
        binding.btnToggleMenu.visibility = if (visible) View.VISIBLE else View.GONE
    }


}
