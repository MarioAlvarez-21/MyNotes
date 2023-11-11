package com.example.mynotes.ui.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.mynotes.R

private lateinit var tvregistrarse : TextView
private lateinit var btIniciarSesion : Button

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fullScreen()
        InitComponent()
        botones()

    }

    private fun InitComponent(){
        tvregistrarse = findViewById(R.id.tvregistrarse)
        btIniciarSesion = findViewById(R.id.button)
    }

    private fun botones(){
        tvregistrarse.setOnClickListener{
            var intent = Intent(this, Registrarse::class.java)
            startActivity(intent)
        }

        btIniciarSesion.setOnClickListener{
            var intent = Intent(this, Inicio::class.java)
            startActivity(intent)
        }
    }

    private fun fullScreen(){
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}