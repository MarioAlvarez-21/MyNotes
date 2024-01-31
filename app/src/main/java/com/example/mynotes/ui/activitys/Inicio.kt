package com.example.mynotes.ui.activitys


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mynotes.R
import com.example.mynotes.databinding.ActivityInicioBinding
import com.example.mynotes.ui.fragments.FavoritosFragment
import com.example.mynotes.ui.fragments.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Inicio : AppCompatActivity() {

    private lateinit var binding: ActivityInicioBinding

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth //Inicia laautenticacion de firebase

        //Funciones de los botones
        botones()

        //Agrega e inicia HomeFragment en BottomNavegationView
        supportFragmentManager.beginTransaction().add(R.id.frameLayout, HomeFragment()).commit()


    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        if (currentFragment !is HomeFragment) {
            // Si el fragmento actual no es el de inicio, navega al fragmento de inicio
            val inicioFragment = HomeFragment()
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, inicioFragment).commit()
        } else {
            super.onBackPressed()
        }
    }

    fun cerrarSesion() {

        auth.signOut()
        Toast.makeText(this, "Cierre de sesion correcto", Toast.LENGTH_SHORT).show()
        // Al cerrar sesion me manda al activity de login
        startActivity(Intent(this, Login::class.java))
        finish()
    }

    private fun botones() {

        //funcionBottomNavegationView
        binding.botonNavegationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homemenu -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, HomeFragment())
                    .commit()

                R.id.favoritesmenu -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, FavoritosFragment())
                    .commit()
                    //cerrarSesion()
            }
            true
        }

    }
}