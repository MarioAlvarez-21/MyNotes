package com.example.mynotes.ui.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mynotes.R
import com.example.mynotes.databinding.ActivityInicioBinding
import com.example.mynotes.ui.fragments.FavoritosFragment
import com.example.mynotes.ui.fragments.HomeFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivityInicioBinding
private lateinit var drawerLayout: DrawerLayout
private lateinit var auth: FirebaseAuth

class Inicio : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fullScreen()

        auth = Firebase.auth //Inicia laautenticacion de firebase

        //Drawer
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        abrir()

        //Agrega e inicia HomeFragment en BottomNavegationView
        supportFragmentManager.beginTransaction().add(R.id.frameLayout, HomeFragment()).commit()
        funcionBottomNavegationView()
    }

    fun cerrarSesion(){

        if(auth != null){
            auth.signOut()
            Toast.makeText(this, "Cierre de sesion correcto", Toast.LENGTH_SHORT).show()
            // Redirect to the login activity
            startActivity(Intent(this, Login::class.java))
            finish()
        }else{
            Toast.makeText(this, "El usuario es nulo???", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Manejar clic en "Home"
                Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_settings -> {
                // Manejar clic en "Settings"
            }
            R.id.nav_logout -> {
                cerrarSesion()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun abrir(){
        val openDrawerButton = findViewById<ImageButton>(R.id.imageButton4)
        openDrawerButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
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