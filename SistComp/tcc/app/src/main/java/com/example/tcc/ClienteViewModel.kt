package com.example.tcc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClienteViewModel : ViewModel() {
    private val _clienteId = MutableLiveData<Int>()
    val clienteId: LiveData<Int> get() = _clienteId

    fun setClienteId(id: Int) {
        _clienteId.value = id
    }
}
