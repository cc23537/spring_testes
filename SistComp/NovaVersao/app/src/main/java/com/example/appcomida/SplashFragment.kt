package com.example.appcomida

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.appcomida.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(3000)
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            // Simulating a 3-second delay for the splash screen
            /*if(isLoggedIn()){
                //findNavController().navigate(R.id.action_splashFragment_to_nav_home)
                (activity as MainActivity).navigateToMain()
            }else {
                //findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }*/

        }
    }

    private fun isLoggedIn(): Boolean {
        val sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs",
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}