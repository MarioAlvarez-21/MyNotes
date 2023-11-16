package com.example.mynotes.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.ui.adapters.MyAdapter
import com.example.mynotes.ui.models.DataModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
        recyclerView()
    }

    fun recyclerView(){
        // Configura el LayoutManager
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerview.layoutManager = layoutManager

        // Crea una lista de datos que se mostrarán en el RecyclerView
        val dataList: List<DataModel> = createDataList()

        // Crea una instancia del adaptador y asígnalo al RecyclerView
        val adapter: MyAdapter = MyAdapter(dataList)
        binding.recyclerview.adapter = adapter
    }

    private fun createDataList(): List<DataModel> {
        val dataList: MutableList<DataModel> = mutableListOf()
        dataList.add(DataModel("Receta macarrones", "Macarrones \nsal \ntomate \nagua", false))
        dataList.add(DataModel("Deuda mama gasoi", "20€", false))
        // Agrega más elementos según sea necesario
        return dataList
    }

}