package com.example.mynotes.ui.activitys

import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import com.example.mynotes.databinding.ActivityNotasOcultasBinding
import com.example.mynotes.ui.adapters.AdapterRecyclerViewNotasOcultas
import com.example.mynotes.ui.models.Nota
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class NotasOcultas : AppCompatActivity() {

    private lateinit var binding : ActivityNotasOcultasBinding

    private lateinit var auth: FirebaseAuth
    private var user = Firebase.auth.currentUser?.uid
    val database = FirebaseDatabase.getInstance()
    val reference = database.getReference("Usuario/${user}/NotasOcultas")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotasOcultasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //fullScreen()

        auth = Firebase.auth

        mostrarNotasRecyclerView()

        botones()

    }

    fun showDialog(){
        // Crear el AlertDialog.Builder
        val builder = AlertDialog.Builder(this)

        // Inflar el layout que contiene tu diseño
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_crear_nota, null)

        // Asignar el diseño al AlertDialog.Builder
        builder.setView(dialogView)

        // Obtener los elementos del diseño
        val titulo = dialogView.findViewById<EditText>(R.id.ET_titulo_dialog)
        val descripcion = dialogView.findViewById<MultiAutoCompleteTextView>(R.id.ET_descripcion_dialog)
        val crear = dialogView.findViewById<Button>(R.id.BT_crear_dialog)
        val cancelar = dialogView.findViewById<Button>(R.id.BT_cancelar_dialog)

        // Crear dialog
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Configurar los botones
        crear.setOnClickListener {
            crearNotaOculta(titulo, descripcion, dialog)

        }
        cancelar.setOnClickListener {
            // Aquí puedes agregar el código para cancelar
            dialog.dismiss()
        }

        // mostrar el AlertDialog
        dialog.show()

    }

    private fun crearNotaOculta(titulo: EditText, descripcion: EditText, dialog: AlertDialog){

        // Obtener el ID del usuario actual
        val user = Firebase.auth.currentUser?.uid

        // Crear un objeto Map para guardar los datos de la nota
        val tituloAsignado = titulo.text.toString()
        val descripcionAsignado = descripcion.text.toString()
        val nota = Nota(reference.push().key, tituloAsignado, descripcionAsignado)

        // Crear una referencia a la base de datos de Firebase
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Usuario/${user}/NotasOcultas")

        // Guardar la nota en Firebase
        reference.child(nota.id?:"").setValue(nota)
            .addOnSuccessListener {
                // La nota se guardó con éxito
                Toast.makeText(this, "Nota creada con éxito", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                true
            }
            .addOnFailureListener { e ->
                // Hubo un error al guardar la nota
                Log.w(ContentValues.TAG, "Error al guardar la nota", e)
                Toast.makeText(this, "Ha habido un problema al crear la nota", Toast.LENGTH_SHORT).show()
                false
            }
    }

    fun mostrarNotasRecyclerView(){
        reference.orderByChild("fecha").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notas = ArrayList<Nota>()
                notas.clear() //PROBAR POR LA PERSISTENCIA DE DATOS FICHA EN EL VIDEO DE YOUTUBE
                for (DataSnapshot in snapshot.children.reversed()) { // Utiliza .reversed() para invertir el orden
                    val titulo = DataSnapshot.child("titulo").getValue(String::class.java)
                    val descripcion = DataSnapshot.child("descripcion").getValue(String::class.java)
                    val id = DataSnapshot.child("id").getValue(String::class.java)
                    val nota = Nota(id, titulo, descripcion)
                    notas.add(nota)

                }
                recyclerView(notas)
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error
            }
        })
    }

    fun recyclerView(notasList: List<Nota>){
            // Configura el LayoutManager
            val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 1)
            binding.recyclerViewNotasOcultas.layoutManager = layoutManager

            // Crea una instancia del adaptador y asígnalo al RecyclerView
            val adapter = AdapterRecyclerViewNotasOcultas(notasList)
            binding.recyclerViewNotasOcultas.adapter = adapter

    }

    private fun botones(){
        binding.IBVolverNotasOcultas.setOnClickListener(){
            finish()
        }
        binding.IBCrearNotaNotasOcultas.setOnClickListener(){
            showDialog()
        }
    }
}