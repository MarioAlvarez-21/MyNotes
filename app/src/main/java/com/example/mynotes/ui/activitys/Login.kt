package com.example.mynotes.ui.activitys

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.DrawableRes
import com.example.mynotes.R
import com.example.mynotes.databinding.ActivityLoginBinding
import com.example.mynotes.ui.models.Usuario
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Pantalla completa
        //fullScreen()

        botones()
        inicioGoogle()


    }

    private fun botones(){
        binding.tvregistrarse.setOnClickListener{
            startActivity(Intent(this, Registrarse::class.java))
        }

        binding.button.setOnClickListener{
            iniciarSesionUsuarioYContraseña()

        }

        binding.btnGoogle.setOnClickListener{
            signIn()
        }
        binding.TVRestablecerContraseA.setOnClickListener(){
            startActivity(Intent(this, RestaurarContrasenia::class.java))
        }
    } //Acciones de los botones

    fun iniciarSesionUsuarioYContraseña(){
        auth = Firebase.auth

        val email = binding.ETEmailLogin.text.toString()
        val password = binding.ETPasswordLogin.text.toString()

        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        if(user?.isEmailVerified == true){
                            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, Inicio::class.java))
                            finish()
                        }else{
                            Toast.makeText(this, "Cuenta pendiente de verificación", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
        }catch (e: Exception){
            Toast.makeText(this, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show()
        }

    }

    // **INICIO DE SESION CON GOOGLE**
    fun inicioGoogle(){

        // Inicializando Firebase Auth
        auth = Firebase.auth

        // Configurando Google SignIn
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Autenticación con Firebase
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Manejo del error
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso

                    // Crear un objeto User para almacenar la información del usuario
                    val user = auth.currentUser
                    val usuario = user?.displayName?: "No disponible"
                    val correo = user?.email?: "No disponible"

                    val userObject = Usuario(usuario, correo)

                    // Obtener una referencia a la base de datos de Firebase
                    val database = Firebase.database.getReference("Usuario/${user?.uid}/Informacion")

                    // Almacenar la información del usuario en la base de datos de Firebase
                    //, actualizar la interfaz de usuario con la información del usuario
                    if (database == null) {
                        database.setValue(userObject)
                        updateUI(user)
                    }else{
                        updateUI(user)
                    }
                } else {
                    // Si el inicio de sesión falla, mostrar un mensaje al usuario.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, Inicio::class.java))
            Toast.makeText(this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            // El usuario es nulo, mostrar un mensaje de error
        }
    }
    // **FIN DE INICIO DE SESION CON GOOGLE**


}