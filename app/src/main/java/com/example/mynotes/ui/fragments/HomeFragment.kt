package com.example.mynotes.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.ui.activitys.Inicio
import com.example.mynotes.ui.adapters.MyAdapter
import com.example.mynotes.ui.models.Nota
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    private var user = Firebase.auth.currentUser?.uid?:""
    private val database = FirebaseDatabase.getInstance()
    private var reference = database.getReference("Notas/${user}")



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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        mostrarNotasRecyclerView()
    }

    fun recyclerView(notasList: List<Nota>){
        // Verifica si el fragmento está adjunto a su actividad
        if (isAdded) {
            // Configura el LayoutManager
            val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 1)
            binding.recyclerview.layoutManager = layoutManager

            // Crea una instancia del adaptador y asígnalo al RecyclerView
            val adapter: MyAdapter = MyAdapter(notasList)
            binding.recyclerview.adapter = adapter
        }else{

        }
    }

    fun mostrarNotasRecyclerView(){
        //var user = Firebase.auth.currentUser?.uid?:""
        //reference = database.getReference("Notas/${user}")
        reference.orderByChild("fecha").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notas = ArrayList<Nota>()
                notas.clear() //PROBAR POR LA PERSISTENCIA DE DATOS FICHA EN EL VIDEO DE YOUTUBE
                for (DataSnapshot in snapshot.children.reversed()) { // Utiliza .reversed() para invertir el orden
                    val titulo = DataSnapshot.child("titulo").getValue(String::class.java)
                    val descripcion = DataSnapshot.child("descripcion").getValue(String::class.java)
                    val nota = Nota(titulo?:"", descripcion?:"")
                    notas.add(nota)

                }
                recyclerView(notas)
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error
            }
        })
    }



}