package com.example.mynotes.ui.activitys

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.example.mynotes.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fullScreen()

        //baseDeDatos()

        navegarLogin()

    }

    fun fullScreen(){
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    fun navegarLogin(){
        binding.botonMain.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
            finish()
        }

    }

    /*fun baseDeDatos(){
        val db = Firebase.firestore

        val city = hashMapOf(
            "name" to "Los Angeles",
            "state" to "CA",
            "country" to "USA"
        )

        db.collection("cities")
            .add(city)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }*/
}