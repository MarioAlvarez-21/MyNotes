package com.example.mynotes.ui.activitys

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.example.mynotes.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivityMainBinding
private lateinit var auth: FirebaseAuth


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //fullScreen()

        auth = Firebase.auth
        usuarioConectado()
        navegarLogin()

    }

    fun usuarioConectado(){//Busco si hay algun usuario logueado activamente
        var user = auth.currentUser
        if (user != null) {
            startActivity(Intent(this, Inicio::class.java));
            finish()
        } else {
            Toast.makeText(this, "No hay usuario autenticado.", Toast.LENGTH_SHORT).show();
        }
    }

    fun navegarLogin(){
        binding.botonMain.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
            finish()
        }

    }
}