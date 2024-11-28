package com.example.appcomida

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _senha = MutableLiveData<String>()
    val senha: LiveData<String> get() = _senha

    private val _idCliente = MutableLiveData<Int>()
    val idCliente: LiveData<Int> get() = _idCliente

    fun setUserCredentials(email: String, senha: String, idCliente: Int) {
        _email.value = email
        _senha.value = senha
        _idCliente.value = idCliente
    }

    fun setIdCliente(id: Int) {
        _idCliente.value = id
    }
}