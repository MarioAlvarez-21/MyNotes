package com.example.mynotes.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.ui.adapters.AdapterRecyclerViewNotas
import com.example.mynotes.ui.models.Nota
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    private lateinit var appBarLayout: AppBarLayout

    private lateinit var auth: FirebaseAuth
    private var user = Firebase.auth.currentUser?.uid
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
        bottomNavigationView = activity?.findViewById(R.id.botonNavegationView)!!
        appBarLayout = activity?.findViewById(R.id.appBarInicio)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        mostrarNotasRecyclerView()
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

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)


                        // El usuario está desplazándose hacia abajo, por lo que ocultamos el BottomNavigationView.
                        if (dy > 0) {
                            bottomNavigationView.animate()
                                .translationY(bottomNavigationView.height.toFloat())
                            appBarLayout.animate().translationY(-appBarLayout.height.toFloat())
                        }

                        // El usuario está desplazándose hacia arriba, por lo que mostramos el BottomNavigationView.
                        else if (dy < 0) {
                            bottomNavigationView.animate().translationY(0f)
                            appBarLayout.animate().translationY(0f)
                        }
                    }
                })
            }

        } else {

        }
    }

    fun mostrarNotasRecyclerView() {
        //var user = Firebase.auth.currentUser?.uid?:""
        //reference = database.getReference("Notas/${user}")
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


}