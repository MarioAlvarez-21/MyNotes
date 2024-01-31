package com.example.mynotes.ui.fragments

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.ui.activitys.IMC
import com.example.mynotes.ui.activitys.Inicio
import com.example.mynotes.ui.activitys.MainActivity
import com.example.mynotes.ui.activitys.NotasOcultas
import com.example.mynotes.ui.adapters.AdapterRecyclerViewNotas
import com.example.mynotes.ui.funciones.Funciones
import com.example.mynotes.ui.models.Nota
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.api.ContextOrBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var auth: FirebaseAuth
    private var user = Firebase.auth.currentUser?.uid
    private val database = FirebaseDatabase.getInstance()
    private var reference = database.getReference("Usuario/${user}/Notas")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        bottomNavigationView = activity?.findViewById(R.id.botonNavegationView)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        mostrarNotasRecyclerView()
        botones()
    }


    fun recyclerView(notasList: List<Nota>) {
        // Verifica si el fragmento está adjunto a su actividad
        if (isAdded) {
            binding.recyclerview.apply {
                // Configura el LayoutManager
                val layoutManager: RecyclerView.LayoutManager =
                    GridLayoutManager(requireContext(), 1)
                binding.recyclerview.layoutManager = layoutManager

                // Crea una instancia del adaptador y asígnalo al RecyclerView
                val adapter = AdapterRecyclerViewNotas(notasList)
                binding.recyclerview.adapter = adapter
            }
        }
    }

    fun mostrarNotasRecyclerView() {
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

    fun botones(){
        binding.BTCrearNotaFragmentHome.setOnClickListener(){
            showDialog()
        }
        binding.BTNotasOcultasFragmentHome.setOnClickListener(){
            startActivity(Intent(requireContext(), NotasOcultas::class.java))
        }
        binding.BTCalcularIMCFragmentHome.setOnClickListener(){
            startActivity(Intent(requireContext(), IMC::class.java))
        }
    }

    fun showDialog(){
        // Crear el AlertDialog.Builder
        val builder = AlertDialog.Builder(requireContext())

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
            //crearNota(titulo, descripcion, dialog)
            Funciones.crearNota(titulo, descripcion, dialog, reference, requireContext())

        }
        cancelar.setOnClickListener {
            // Aquí puedes agregar el código para cancelar
            dialog.dismiss()
        }

        // mostrar el AlertDialog
        dialog.show()

    }


}