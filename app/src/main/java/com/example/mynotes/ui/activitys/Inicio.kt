package com.example.mynotes.ui.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.mynotes.R
import com.example.mynotes.databinding.ActivityInicioBinding
import com.example.mynotes.ui.fragments.FavoritosFragment
import com.example.mynotes.ui.fragments.HomeFragment

private lateinit var binding: ActivityInicioBinding

class Inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fullScreen()

        //Agrega e inicia HomeFragment
        supportFragmentManager.beginTransaction().add(R.id.frameLayout, HomeFragment()).commit()
        funcionBottomNavegationView()
    }

    fun funcionBottomNavegationView(){
        binding.botonNavegationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.homemenu -> supportFragmentManager.beginTransaction().replace(R.id.frameLayout, HomeFragment()).commit()
                R.id.favoritesmenu -> supportFragmentManager.beginTransaction().replace(R.id.frameLayout, FavoritosFragment()).commit()
            }
            true
        }

    }

    fun fullScreen(){
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}