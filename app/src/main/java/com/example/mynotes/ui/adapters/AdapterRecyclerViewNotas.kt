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
import com.example.mynotes.ui.activitys.reference
import com.example.mynotes.ui.models.Nota
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class AdapterRecyclerViewNotas(private val dataList: List<Nota>) : RecyclerView.Adapter<AdapterRecyclerViewNotas.ViewHolder>() {

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
                editarNota(contexto, data.id!!, et_titulo.text.toString(), et_descripcion.text.toString())
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
            eliminarNota(contexto, data.id!!)
        }
        holder.ocultar.setOnClickListener(){
            val id = data.id!!
            val titulo = holder.titulo.text.toString()
            val descripcion = holder.descripcion.text.toString()
            ocultarNota(contexto, id, titulo, descripcion)
            eliminarNota(contexto, id)
        }
    }

    private fun eliminarNota(context: Context, notaId: String){

        val user = FirebaseAuth.getInstance().currentUser

        val database = FirebaseDatabase.getInstance().getReference("Notas");

        if(user != null){
            val userId = user.uid
            val likesRef = database.child(userId).child(notaId)
            likesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        if (snapshot.exists()) {
                            // El modelo ya está en la lista de "me gusta", eliminarlo
                            likesRef.removeValue();
                            Toast.makeText(context, "Nota eliminada con exito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "¿Nota no existe?", Toast.LENGTH_SHORT).show();
                        }
                    }catch (e:Exception){
                        Toast.makeText(context, "Error al eliminar nota", Toast.LENGTH_SHORT).show();
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar error
                }
            })
        }

    }

    private fun editarNota(context: Context, notaId: String, nuevoTitulo: String, nuevaDescripcion: String){

        val user = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance().getReference("Notas");


        if(user != null){
            val userId = user.uid
            val notaRef = database.child(userId).child(notaId)
            notaRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        if (snapshot.exists()) {
                            // La nota existe, actualizarla
                            val notaActualizada = Nota(notaId, nuevoTitulo, nuevaDescripcion)
                            notaRef.setValue(notaActualizada)
                            Toast.makeText(context, "Nota actualizada", Toast.LENGTH_SHORT).show();
                        } else {
                            // La nota no existe, manejar el error
                            Toast.makeText(context, "¿La nota no existe?", Toast.LENGTH_SHORT).show();
                        }
                    }catch (e:Exception){
                        Toast.makeText(context, "Error al editar nota", Toast.LENGTH_SHORT).show();
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar error
                }
            })
        }

    }

    private fun ocultarNota(context: Context, id: String, titulo:String, descripcion:String){

        // Obtener el ID del usuario actual
        val user = Firebase.auth.currentUser?.uid

        // Crear un objeto para guardar los datos de la nota
        val nota = Nota(id, titulo, descripcion)

        // Crear una referencia a la base de datos de Firebase
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("NotasOcultas/${user}")

        // Si el usuario es distinto de null guardar la nota en Firebase
        if(user != null){
            reference.child(nota.id!!).setValue(nota)
                .addOnSuccessListener {
                    // La nota se guardó con éxito
                    Toast.makeText(context, "Nota oculta con éxito", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // Hubo un error al guardar la nota
                    Log.w(ContentValues.TAG, "Error al ocultar la nota", e)
                    Toast.makeText(context, "Ha habido un problema al ocultar la nota", Toast.LENGTH_SHORT).show()
                }
        }else{
            Toast.makeText(context, "Usuario null", Toast.LENGTH_SHORT).show()
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
