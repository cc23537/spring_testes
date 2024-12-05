package com.example.tcc

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.tcc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verificação de login
        val isLoggedIn = checkUserLoggedIn()

        // Carregar o fragmento inicial
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

        // Botão de alternância para abrir/fechar a barra lateral
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
            replaceFragment(GalleryFragment(), true)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.btnWelcome.setOnClickListener {
            replaceFragment(WelcomeFragment(), true)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
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
