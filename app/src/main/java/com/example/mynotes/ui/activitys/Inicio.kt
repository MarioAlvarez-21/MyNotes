package com.example.mynotes.ui.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.mynotes.R
import com.example.mynotes.ui.fragments.FavoritosFragment
import com.example.mynotes.ui.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

lateinit var BNV : BottomNavigationView

class Inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        fullScreen()

        BNV = findViewById(R.id.botonNavegationView)

        supportFragmentManager.beginTransaction().add(R.id.frameLayout, HomeFragment()).commit()
        funcionBottomNavegationView()
    }

    fun funcionBottomNavegationView(){
        BNV.setOnItemSelectedListener{
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