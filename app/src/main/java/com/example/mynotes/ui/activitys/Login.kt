package com.example.mynotes.ui.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.mynotes.databinding.ActivityLoginBinding

private lateinit var binding: ActivityLoginBinding

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Pantalla completa
        fullScreen()

        botones()
    }

    private fun botones(){
        binding.tvregistrarse.setOnClickListener{
            var intent = Intent(this, Registrarse::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener{
            startActivity(Intent(this, Inicio::class.java))
        }
    }

    private fun fullScreen(){
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}