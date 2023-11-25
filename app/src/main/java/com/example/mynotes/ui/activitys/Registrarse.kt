package com.example.mynotes.ui.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.WindowManager
import android.widget.Toast
import com.example.mynotes.R
import com.example.mynotes.databinding.ActivityRegistrarseBinding
import com.example.mynotes.ui.models.Usuario
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Registrarse : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarseBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var email : String
    private lateinit var usuario :String
    private lateinit var repetirPassword : String
    private lateinit var telefono :String
    private lateinit var password :String
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fullScreen()

        botones()

    }

    private fun fullScreen(){
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    fun botones(){
        binding.btnRegistrar.setOnClickListener{
            registrarUsuario()
        }
        binding.imageButton.setOnClickListener{
            finish()
        }
    }

    fun registrarUsuario(){
        auth = Firebase.auth
        database = Firebase.database.reference

        if(validarUsuario()){
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            sendEmailVerification()
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            val userId = user?.uid

                            val userData = Usuario(usuario, email, telefono)

                            // Store the user data in Firebase Realtime Database
                            database.child("Usuarios").child(userId!!).setValue(userData)

                            Toast.makeText(this, "Usuario registrado con éxito, revise su correo para activar la cuenta", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "El email ya existe, prueba con otro", Toast.LENGTH_SHORT).show()
                        }
                    }
            }catch (e:Exception){
                Toast.makeText(this, "Error al crear usuario", Toast.LENGTH_SHORT).show()
            }

        }
    }

    //Envia un correo al usuario para que verefique su cuenta
    private fun sendEmailVerification(){
        var user = auth.currentUser!!
        user.sendEmailVerification().addOnCompleteListener(this){task ->
            if (task.isSuccessful){
                Toast.makeText(this, "Revisa tu correo para verificar la cuenta", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Error al enviar correo de verificación", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun validarUsuario():Boolean{
        email = binding.ETEmailRegistro.text.toString()
        password = binding.ETPasswordRegistro.text.toString()
        usuario = binding.ETUsuarioRegistro.text.toString()
        repetirPassword = binding.ETRepetirPasswordRegistro.text.toString()
        telefono = binding.ETTelefonoRegistro.text.toString()

        if(usuario.isEmpty()){
            binding.ETUsuarioRegistro.setError("Ingresa un nombre de usuario")
            return false
        }
        if(email.isEmpty()){
            binding.ETEmailRegistro.setError("Ingresa un email")
            return false
        }else if(!isValidEmail(email)){
            binding.ETEmailRegistro.setError("Ingresa un formato valido de email")
            return false
        }
        if(password.isEmpty()){
            binding.ETPasswordRegistro.setError("Ingrese una contraseña")
            return false
        }else if(password.length < 6){
            binding.ETPasswordRegistro.setError("La contraseña no puede ser inferior a 6 caracteres")
            return false
        }

        if(repetirPassword.isEmpty()){
            binding.ETRepetirPasswordRegistro.setError("Ingrese su contraseña")
            return false
        }else if(!repetirPassword.equals(password)){
            binding.ETRepetirPasswordRegistro.setError("Las contraseñas no coinciden")
            return false
        }
        return true

    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}