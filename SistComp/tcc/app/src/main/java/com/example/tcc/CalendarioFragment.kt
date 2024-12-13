package com.example.tcc

import CaixaAzulDecorator
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.appcompat.app.AlertDialog
import com.example.tcc.api.Rotas
import com.example.tcc.api.getRetrofit
import com.example.tcc.databinding.FragmentCalendarioBinding
import com.example.tcc.dataclass.AlimentoEstocadoDto
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.launch
import java.time.DateTimeException
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
class CalendarioFragment : Fragment() {

    private var _binding: FragmentCalendarioBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var azulDecorator: CaixaAzulDecorator
    private lateinit var userViewModel: ClienteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarioBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userViewModel = ViewModelProvider(requireActivity()).get(ClienteViewModel::class.java)
        calendarView = binding.calendarView
        azulDecorator = CaixaAzulDecorator(mutableListOf(), requireContext().getDrawable(R.drawable.caixa_azul)!!)
        calendarView.addDecorator(azulDecorator)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingAddAlimentos.setOnClickListener {
            //Toast.makeText(requireContext(), "Adicionar alimentos ainda não implementado!", Toast.LENGTH_SHORT).show()
            val add = AddCalendarioFragment()
            add.show(parentFragmentManager, "AddDialog")
        }

        calendarView.setOnDateChangedListener { _, date, _ ->
            println("Data clicada: $date")
            handleDateClick(date)
        }

        // Faz a chamada da API quando o fragmento é exibido
        fetchAlimentosEstocados()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchAlimentosEstocados() {
        println("Iniciando fetchAlimentosEstocados")
        val retrofit = getRetrofit()
        val apiService = retrofit.create(Rotas::class.java)
        val idCliente = userViewModel.clienteId.value
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
                        val datas = alimentosEstocadosResponses.mapNotNull { alimento ->
                            // Ajustar mês da validade para corresponder ao CalendarDay
                            val validade = LocalDate.parse(alimento.validade)
                            validade
                        }

                        updateCalendar(datas)

                        // Filtra os alimentos do dia atual
                        val alimentosDoDia = filterFoodsForToday(alimentosEstocadosResponses)

                        // Atualiza o TextView com os alimentos do dia
                        updateTodayFoodsLabel(alimentosDoDia)
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
            CalendarDay.from(date.year, date.monthValue - 1, date.dayOfMonth) // Ajustar o mês para 0-11
        }
        println("Datas no calendário: $azulDays")

        azulDecorator.dates.clear()
        azulDecorator.dates.addAll(azulDays)
        calendarView.invalidateDecorators()
        println("Decorators atualizados com as datas: ${azulDecorator.dates}")
    }

    private fun handleDateClick(date: CalendarDay) {
        val dataSelecionada: LocalDate? = try {
            LocalDate.of(date.year, date.month + 1, date.day) // Ajustando o mês para 1-12 ao criar LocalDate
        } catch (e: DateTimeException) {
            println("Erro ao criar LocalDate: ${e.message}")
            null
        }

        if (dataSelecionada != null) {
            lifecycleScope.launch {
                val alimentosDoDia = fetchAlimentosPorData(dataSelecionada)
                showDateInfoDialog(dataSelecionada, alimentosDoDia) // Passe LocalDate diretamente
            }
            val diaSelecionado = "${date.day}/${date.month + 1}/${date.year}" // Ajustando o mês para exibição correta
            Toast.makeText(requireContext(), "Dia selecionado: $diaSelecionado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Data inválida selecionada.", Toast.LENGTH_SHORT).show()
        }
    }


    private suspend fun fetchAlimentosPorData(data: LocalDate): List<AlimentoEstocadoDto> {
        val retrofit = getRetrofit()
        val apiService = retrofit.create(Rotas::class.java)
        val idCliente = userViewModel.clienteId.value ?: return emptyList()
        return try {
            val response = apiService.getAllAlimentosEstocados(idCliente)
            if (response.isSuccessful) {
                val alimentos = response.body()?.filter {
                    LocalDate.parse(it.validade).isEqual(data)
                } ?: emptyList()
                println("Alimentos na data $data: $alimentos")
                alimentos
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun showDateInfoDialog(dataSelecionada: LocalDate, alimentosDoDia: List<AlimentoEstocadoDto>) {
        val message = if (alimentosDoDia.isNotEmpty()) {
            alimentosDoDia.joinToString("\n") { alimento ->
                val diasRestantes = diasFaltantes(dataSelecionada.year, dataSelecionada.monthValue, dataSelecionada.dayOfMonth)
                val diasMensagem = if (diasRestantes < 0) {
                    "ESSE ALIMENTO JÁ VENCEU"
                } else {
                    "⏳ DIAS FALTANTES: $diasRestantes"
                }

                """
            ➡️ **NOME**: ${alimento.nomeAlimento}
            🔥 CALORIAS: ${alimento.calorias} kcal
            📝 ESPECIFICAÇÕES: ${alimento.especificacoes}
            🗓️ VALIDADE: ${alimento.validade}
            📦 QUANTIDADE: ${alimento.quantidadeEstoque}
            $diasMensagem
            """.trimIndent()
            }
        } else {
            "Nenhum alimento registrado para esta data."
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Alimentos no dia ${dataSelecionada.dayOfMonth}/${dataSelecionada.monthValue}/${dataSelecionada.year}")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }


    private fun updateTodayFoodsLabel(alimentosDoDia: List<AlimentoEstocadoDto>) {
        val todayFoodsTextView = binding.tvExpiredFoods

        if (alimentosDoDia.isNotEmpty()) {
            val alimentosList = alimentosDoDia.joinToString(separator = "\n") { "• ${it.nomeAlimento}" }
            todayFoodsTextView.text = "Alimentos que vencem Hoje:\n$alimentosList"
        } else {
            todayFoodsTextView.text = "Alimentos para hoje: Nenhum"
        }
    }

    private fun diasFaltantes(year: Int, month: Int, day: Int): Long {
        val dataAtual: LocalDate = LocalDate.now()
        val dataValidade = LocalDate.of(year, month, day)

        return ChronoUnit.DAYS.between(dataAtual, dataValidade)
    }

    private fun filterFoodsForToday(alimentos: List<AlimentoEstocadoDto>): List<AlimentoEstocadoDto> {
        val hoje = LocalDate.now()
        return alimentos.filter { LocalDate.parse(it.validade).isEqual(hoje) }
    }
}
