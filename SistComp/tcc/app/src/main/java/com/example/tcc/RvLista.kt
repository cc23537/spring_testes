package com.example.tcc

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tcc.databinding.ListaAddBinding
import com.example.tcc.dataclass.Compra
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RvLista(private val compraList: ArrayList<Compra>) : RecyclerView.Adapter<RvLista.ViewHolder>() {

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
        val currentItem = compraList[position]

        holder.binding.apply {
            // Exibe o nome do alimento e a quantidade
            Alimento.text = currentItem.alimentoASerComprado.nomeAlimento // Nome do alimento
            quantidade.text = "Quantidade: ${currentItem.quantidade}" // Quantidade
        }
    }

    fun deleteItem(position: Int, context: Context): String {
        val nomeAlimento = compraList[position].alimentoASerComprado.nomeAlimento // Nome do alimento da compra
        MaterialAlertDialogBuilder(context)
            .setTitle("Deletar Alimento Permanentemente")
            .setMessage("Você tem certeza que deseja excluir $nomeAlimento?")
            .setPositiveButton("Sim") { _, _ ->
                // Remove o item da lista e notifica o RecyclerView
                compraList.removeAt(position)
                notifyItemRemoved(position)
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