package com.example.mynotes.ui.adapters

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import com.example.mynotes.ui.funciones.Funciones
import com.example.mynotes.ui.models.Nota
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class AdapterRecyclerViewNotas(private val dataList: List<Nota>) : RecyclerView.Adapter<AdapterRecyclerViewNotas.ViewHolder>() {

    private var user = Firebase.auth.currentUser?.uid
    private val database = FirebaseDatabase.getInstance()
    private var reference = database.getReference("Usuario/${user}/Notas")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        val contexto = holder.itemView.context
        holder.titulo.text = data.titulo
        holder.descripcion.text = data.descripcion
        holder.descripcion.visibility = if (data.expand) View.VISIBLE else View.GONE
        holder.eliminar.visibility = if (data.expand) View.VISIBLE else View.GONE
        holder.editar.visibility = if (data.expand) View.VISIBLE else View.GONE
        holder.ocultar.visibility = if (data.expand) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            data.expand = !data.expand
            notifyItemChanged(position)
        }

        holder.editar.setOnClickListener(){
            val builder = AlertDialog.Builder(contexto)
            val inflater = LayoutInflater.from(contexto)
            val dialogView = inflater.inflate(R.layout.dialog_crear_nota, null)
            builder.setView(dialogView)

            val et_titulo = dialogView.findViewById<EditText>(R.id.ET_titulo_dialog)
            val et_descripcion = dialogView.findViewById<MultiAutoCompleteTextView>(R.id.ET_descripcion_dialog)
            val bt_crear = dialogView.findViewById<Button>(R.id.BT_crear_dialog)
            val bt_cancelar = dialogView.findViewById<Button>(R.id.BT_cancelar_dialog)
            val textoDialog = dialogView.findViewById<TextView>(R.id.tv_dialog)

            et_titulo.setText(holder.titulo.text)
            et_descripcion.setText(holder.descripcion.text)
            textoDialog.setText("Editar nota")
            bt_crear.setText("Editar")

            // Crear dialog
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // Configurar los botones
            bt_crear.setOnClickListener {
                Funciones.editarNota(contexto, data.id!!, et_titulo.text.toString(), et_descripcion.text.toString())
                dialog.dismiss()

            }
            bt_cancelar.setOnClickListener {
                // Aquí puedes agregar el código para cancelar
                dialog.dismiss()
            }

            // mostrar el AlertDialog
            dialog.show()
        }

        holder.eliminar.setOnClickListener(){
            Funciones.eliminarNota(contexto, data.id!!, reference)
        }
        holder.ocultar.setOnClickListener(){
            val id = data.id!!
            val titulo = holder.titulo.text.toString()
            val descripcion = holder.descripcion.text.toString()
            Funciones.ocultarNota(contexto, id, titulo, descripcion)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titulo: TextView = itemView.findViewById(R.id.tvTituloCard)
        var descripcion: TextView = itemView.findViewById(R.id.tvDescripcionCard)
        var eliminar: ImageButton = itemView.findViewById(R.id.IB_borrarNota_CardView)
        var editar: ImageButton = itemView.findViewById(R.id.IB_EditarNota_CardView)
        var ocultar: ImageButton = itemView.findViewById(R.id.IB_OcultarNota_CardView)

    }
}
