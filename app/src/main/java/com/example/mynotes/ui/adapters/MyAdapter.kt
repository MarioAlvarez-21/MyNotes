package com.example.mynotes.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import com.example.mynotes.ui.models.DataModel

class MyAdapter(private val dataList: List<DataModel>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.titulo.text = data.titulo
        holder.descripcion.text = data.descripcion
        holder.descripcion.visibility = if (data.expand) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            data.expand = !data.expand
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titulo: TextView = itemView.findViewById(R.id.tvTituloCard)
        var descripcion: TextView = itemView.findViewById(R.id.tvDescripcionCard)
    }
}
