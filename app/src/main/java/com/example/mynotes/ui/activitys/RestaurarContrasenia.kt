package com.example.mynotes.ui.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.mynotes.R
import com.example.mynotes.databinding.ActivityRestaurarContraseniaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RestaurarContrasenia : AppCompatActivity() {

    private lateinit var binding : ActivityRestaurarContraseniaBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurarContraseniaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fullScreen()

        botones()

    }

    private fun sendPasswordReset(email:String){
        auth = Firebase.auth
        auth.sendPasswordResetEmail(email).addOnCompleteListener(){task ->
            if(task.isSuccessful){
                Toast.makeText(this, "Correo enviado", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Error de envio de correo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun botones(){
        binding.IBVolverRestablecerContraseA.setOnClickListener(){
            finish()
        }
        binding.BTEnviarRestablecerContrasenia.setOnClickListener(){
            val email = binding.ETRecordarContraseA.text.toString()
            sendPasswordReset(email)
        }
    }

    private fun fullScreen(){
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    } //Quitar el statusbar y actionbar
}