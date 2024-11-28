package com.example.appcomida.ui.Calendario

import CaixaAzulDecorator
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.appcomida.R
import com.example.appcomida.UserViewModel
import com.example.appcomida.databinding.FragmentCalendarioBinding
import com.example.appcomida.dataclass.Alimento
import com.example.appcomida.dataclass.AlimentoASerEstocado
import com.example.appcomida.dataclass.AlimentoEstocado
import com.example.appcomida.rotas.AlimentoEstocadoRotas
import com.example.appcomida.dataclass.AlimentoEstocadoResponse
import com.example.appcomida.dataclass.ApiResponse
import com.example.appcomida.dataclass.ClienteApi
import com.example.appcomida.rotas.ComprasRotas
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import getRetrofit
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class CalendarioFragment : Fragment() {

    private var _binding: FragmentCalendarioBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var azulDecorator: CaixaAzulDecorator
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarioBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        calendarView = binding.calendarView

        // Inicializa o decorador
        azulDecorator = CaixaAzulDecorator(mutableListOf(), requireContext().getDrawable(R.drawable.caixa_azul)!!)
        calendarView.addDecorator(azulDecorator)

        fetchAlimentosEstocados()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingAddAlimentos.setOnClickListener {
            Toast.makeText(requireContext(), "Adicionar alimentos ainda não implementado!", Toast.LENGTH_SHORT).show()
        }

        calendarView.setOnDateChangedListener { _, date, _ ->
            handleDateClick(date)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchAlimentosEstocados() {
        println("Iniciando fetchAlimentosEstocados")
        val retrofit = getRetrofit()
        val apiService = retrofit.create(AlimentoEstocadoRotas::class.java)
        val alimentosEstocadosList = arrayListOf<AlimentoEstocadoResponse>() // Lista para armazenar os alimentos estocados

        val idCliente = userViewModel.idCliente.value
        println("ID Cliente: $idCliente")

        if (idCliente == null) {
            println("Cliente não autenticado!")
            return
        }
        lifecycleScope.launch {
            try {
                val response = apiService.getAllAlimentosEstocados(idCliente)
                if (response.isSuccessful) {
                    val alimentosEstocadosResponses = response.body()
                    if (alimentosEstocadosResponses != null) {
                        // Processar a lista de AlimentoEstocadoResponse
                        alimentosEstocadosResponses.forEach { alimentoEstocado ->
                            println(alimentoEstocado)
                        }
                    }
                } else {
                    println("Erro ao obter dados: ${response.message()}")
                }
            } catch (e: Exception) {
                println("Erro ao obter dados: ${e.message}")
            }
        }


    }




    private fun updateCalendar(datas: List<LocalDate>) {
        val azulDays = datas.map { date ->
            CalendarDay.from(date.year, date.monthValue - 1, date.dayOfMonth)
        }

        azulDecorator.dates.clear()
        azulDecorator.dates.addAll(azulDays)
        calendarView.invalidateDecorators()
    }

    private fun handleDateClick(date: CalendarDay) {
        val diaSelecionado = "${date.day}/${date.month + 1}/${date.year}"
        Toast.makeText(requireContext(), "Dia selecionado: $diaSelecionado", Toast.LENGTH_SHORT).show()
    }

    private fun filterFoodsForToday(alimentosEstocados: List<AlimentoEstocado>): List<AlimentoEstocado> {
        val hoje = LocalDate.now()
        return alimentosEstocados.filter { it.validade == hoje }
    }

    private fun updateTodayFoodsLabel(alimentosDoDia: List<AlimentoEstocado>) {
        val todayFoodsTextView = binding.tvExpiredFoods

        if (alimentosDoDia.isNotEmpty()) {
            val alimentosList = alimentosDoDia.joinToString(separator = "\n") {
                "• ${it.alimentoASerEstocado?.nomeAlimento ?: "Sem nome"}"
            }
            todayFoodsTextView.text = "Alimentos Estocados que vencem Hoje:\n$alimentosList"
        } else {
            todayFoodsTextView.text = "Nenhum alimento estocado vence hoje."
        }
    }

}
