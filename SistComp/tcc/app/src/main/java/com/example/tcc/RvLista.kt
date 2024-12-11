package com.example.tcc

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tcc.controller.apiService
import com.example.tcc.databinding.ListaAddBinding
import com.example.tcc.dataclass.Compra
import com.example.tcc.dataclass.CompraWrapper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RvLista(private val compraList: ArrayList<CompraWrapper>) : RecyclerView.Adapter<RvLista.ViewHolder>() {

    class ViewHolder(val binding: ListaAddBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListaAddBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return compraList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = compraList[position].compra

        holder.binding.apply {
            // Exibe o nome do alimento e a quantidade
            Alimento.text = currentItem.alimentoASerComprado.nomeAlimento // Nome do alimento
            quantidade.text = "Quantidade: ${currentItem.quantidade}" // Quantidade
        }
    }

    fun deleteItem(position: Int, context: Context): String {
        val compraWrapper = compraList[position]
        val idCompra = compraWrapper.idCompra // Recupera o ID da compra a partir do CompraWrapper
        val nomeAlimento = compraWrapper.compra.alimentoASerComprado.nomeAlimento // Nome do alimento da compra

        MaterialAlertDialogBuilder(context)
            .setTitle("Deletar Alimento Permanentemente")
            .setMessage("Você tem certeza que deseja excluir $nomeAlimento?")
            .setPositiveButton("Sim") { _, _ ->
                // Chama a função da API para deletar a compra
                val call = apiService.DeletaCompra(idCompra)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            // Remove o item da lista e notifica o RecyclerView
                            compraList.removeAt(position)
                            notifyItemRemoved(position)
                        } else {
                            // Handle the error
                            Toast.makeText(context, "Erro ao deletar a compra", Toast.LENGTH_SHORT).show()
                            notifyItemChanged(position)
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        // Handle the failure
                        Toast.makeText(context, "Falha ao deletar a compra", Toast.LENGTH_SHORT).show()
                        notifyItemChanged(position)
                    }
                })
            }
            .setNegativeButton("Não") { dialog, _ ->
                // Reverte o swipe
                dialog.dismiss()
                notifyItemChanged(position)
            }
            .show()
        return nomeAlimento
    }
}
