package com.example.mynotes.ui.funciones

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ReferenciasFirebase {

    companion object{
        var userActual = Firebase.auth.currentUser?.uid

        fun referenciaBaseDatos(referencia: String): DatabaseReference {
            return FirebaseDatabase.getInstance().getReference(referencia)
        }
    }
}