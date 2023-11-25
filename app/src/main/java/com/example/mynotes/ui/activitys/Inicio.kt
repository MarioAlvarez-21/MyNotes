package com.example.mynotes.ui.activitys

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.example.mynotes.R
import com.example.mynotes.databinding.ActivityInicioBinding
import com.example.mynotes.databinding.DialogCrearNotaBinding
import com.example.mynotes.ui.fragments.FavoritosFragment
import com.example.mynotes.ui.fragments.HomeFragment
import com.example.mynotes.ui.models.Nota
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivityInicioBinding
val database = FirebaseDatabase.getInstance()
val reference = database.getReference("Notas/ebgLaec2tbZ5KFTFH6vuv5zvFer2")
private lateinit var auth: FirebaseAuth

class Inicio : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fullScreen()

        auth = Firebase.auth //Inicia laautenticacion de firebase

        //Funciones de los botones
        botones()

        //Drawer
        binding.navView.setNavigationItemSelectedListener(this)


        //Agrega e inicia HomeFragment en BottomNavegationView
        supportFragmentManager.beginTransaction().add(R.id.frameLayout, HomeFragment()).commit()


    }

    fun cerrar(){
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
            }
        })
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
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun botones(){

        //funcionBottomNavegationView
        binding.botonNavegationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.homemenu -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, HomeFragment())
                    .commit()
                R.id.favoritesmenu -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, FavoritosFragment())
                    .commit()
            }
            true
        }

        //Abrir el navegationDrawer
        binding.navegationDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.BTCrearNotaInicio.setOnClickListener(){
            showDialog()
        }

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
            crearNota(titulo, descripcion, dialog)

        }
        cancelar.setOnClickListener {
            // Aquí puedes agregar el código para cancelar
            dialog.dismiss()
        }

        // mostrar el AlertDialog
        dialog.show()

    }

    private fun crearNota(titulo:EditText, descripcion:EditText, dialog: AlertDialog){

        // Crear un objeto Map para guardar los datos de la nota
        val tituloAsignado = titulo.text.toString()
        val descripcionAsignado = descripcion.text.toString()
        val nota = Nota(tituloAsignado?:"Sin titulo", descripcionAsignado?:"Sin descripcion")

        // Guardar la nota en Firebase
        reference.push().setValue(nota)
            .addOnSuccessListener {
                // La nota se guardó con éxito
                Toast.makeText(this, "Nota creada con éxito", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                true
            }
            .addOnFailureListener { e ->
                // Hubo un error al guardar la nota
                Log.w(TAG, "Error al guardar la nota", e)
                Toast.makeText(this, "Ha habido un problema al crear la nota", Toast.LENGTH_SHORT).show()
                false
            }
    }


    fun fullScreen(){
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}