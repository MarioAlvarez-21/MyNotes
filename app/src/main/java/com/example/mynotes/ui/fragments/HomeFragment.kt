package com.example.mynotes.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import com.example.mynotes.ui.adapters.MyAdapter
import com.example.mynotes.ui.models.DataModel


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerview)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView()
    }

    fun recyclerView(){
        // Configura el LayoutManager
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager

        // Crea una lista de datos que se mostrarán en el RecyclerView
        val dataList: List<DataModel> = createDataList()

        // Crea una instancia del adaptador y asígnalo al RecyclerView
        val adapter: MyAdapter = MyAdapter(dataList)
        recyclerView.adapter = adapter
    }

    private fun createDataList(): List<DataModel> {
        val dataList: MutableList<DataModel> = mutableListOf()
        dataList.add(DataModel("Título 1", "Descripción 1"))
        dataList.add(DataModel("Título 2", "Descripción 2"))
        // Agrega más elementos según sea necesario
        return dataList
    }

}