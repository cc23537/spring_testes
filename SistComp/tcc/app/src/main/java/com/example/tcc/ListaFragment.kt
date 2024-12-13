package com.example.tcc

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcc.api.Rotas
import com.example.tcc.api.getRetrofit
import com.example.tcc.databinding.FragmentListaBinding
import com.example.tcc.dataclass.Alimento
import com.example.tcc.dataclass.AlimentoASerComprado
import com.example.tcc.dataclass.ApiResponse
import com.example.tcc.dataclass.ClientePft
import com.example.tcc.dataclass.Compra
import com.example.tcc.dataclass.CompraWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ListaFragment : Fragment() {

    private var _binding: FragmentListaBinding? = null
    private val binding get() = _binding!!
    private lateinit var alimentosList: ArrayList<CompraWrapper>
    private lateinit var userViewModel: ClienteViewModel // ViewModel do usuário

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel = ViewModelProvider(this).get(ListaViewModel::class.java)
        _binding = FragmentListaBinding.inflate(inflater, container, false)

        // Inicializando o UserViewModel
        userViewModel = ViewModelProvider(requireActivity()).get(ClienteViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAlimentos.layoutManager = LinearLayoutManager(requireContext())

        fetchCompras()
        deletar()

        binding.rvAlimentos.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
        }

        parentFragmentManager.setFragmentResultListener("adicionarAlimento", this) { _, _ ->
            fetchCompras()  // Atualiza a lista chamando novamente fetchCompras
        }

        binding.floatingActionButton.setOnClickListener {
            val add = AddListaDialogFragment()
            add.show(parentFragmentManager, "AddDialog")
        }
    }


    private fun fetchCompras() {
        println("Iniciando fetchCompras")
        val retrofit = getRetrofit()
        val apiService = retrofit.create(Rotas::class.java)
        alimentosList = arrayListOf()

        val idCliente = userViewModel.clienteId.value
        println("ID Cliente: $idCliente")

        if (idCliente == null) {
            showError("Cliente não autenticado!")
            return
        }

        apiService.listagemCompras(idCliente).enqueue(object : Callback<List<ApiResponse>> {
            override fun onResponse(call: Call<List<ApiResponse>>, response: Response<List<ApiResponse>>) {
                println("Resposta da API recebida")
                if (response.isSuccessful) {
                    val apiResponses = response.body()
                    println("API Responses: $apiResponses")
                    if (apiResponses != null) {
                        alimentosList.clear()
                        // Mapeia a lista de ApiResponse para a lista de CompraWrapper
                        val compras = apiResponses.map { CompraWrapper(it.idCompra, mapApiResponseToCompra(it)) }
                        alimentosList.addAll(ArrayList(compras))
                        println(alimentosList)

                        // Passa a lista de CompraWrapper para o adapter
                        val rvAdapter = RvLista(alimentosList)
                        binding.rvAlimentos.adapter = rvAdapter
                        rvAdapter.notifyDataSetChanged()
                        println("Adapter configurado com sucesso")
                    } else {
                        println("API Responses retornadas são nulas")
                    }
                } else {
                    showError("Erro ao obter dados: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ApiResponse>>, t: Throwable) {
                println("Falha na solicitação: ${t.message}")
            }
        })
    }




    private fun mapApiResponseToCompra(apiResponse: ApiResponse): Compra {
        return Compra(
            alimentoASerComprado = AlimentoASerComprado(
                nomeAlimento = apiResponse.alimentoASerComprado.nomeAlimento,
                calorias = (apiResponse.alimentoASerComprado.calorias ?: 0.0).toInt(),
                cliente = apiResponse.alimentoASerComprado.cliente.idCliente // Aqui você pode usar o cliente se necessário
            ),
            quantidade = apiResponse.quantidade,
            cliente = ClientePft(apiResponse.cliente.idCliente),
        )
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun deletar() {
        val swipeBackground = ColorDrawable(Color.RED)
        val deleteIcon: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)

        val swipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Obtém a posição do item
                val position = viewHolder.adapterPosition

                // Obtém o CompraWrapper associado à posição
                val compraWrapper = alimentosList[position]

                // Obtém o idCompra
                val idCompra = compraWrapper.idCompra
                Toast.makeText(requireContext(), "ID da Compra = $idCompra", Toast.LENGTH_SHORT).show()

                // Deleta o item da lista (exemplo do seu código)
                val adapter = binding.rvAlimentos.adapter as RvLista
                val resposta: String = adapter.deleteItem(position, requireContext())
                Toast.makeText(requireContext(), "Alimento = $resposta", Toast.LENGTH_SHORT).show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                val itemView = viewHolder.itemView
                val backgroundCornerOffset = 20

                val paint = Paint().apply {
                    color = Color.rgb(253, 105, 105)
                    isAntiAlias = true
                }

                val rectF = RectF(
                    itemView.right + dX - backgroundCornerOffset,
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat()
                )
                val cornerRadius = 30f

                if (dX < 0) {
                    c.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)

                    val iconMargin = (itemView.height - deleteIcon!!.intrinsicHeight) / 2
                    val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
                    val iconBottom = iconTop + deleteIcon.intrinsicHeight
                    val iconLeft = itemView.right - iconMargin - deleteIcon.intrinsicWidth
                    val iconRight = itemView.right - iconMargin
                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                    deleteIcon.draw(c)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipe)
        itemTouchHelper.attachToRecyclerView(binding.rvAlimentos)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}