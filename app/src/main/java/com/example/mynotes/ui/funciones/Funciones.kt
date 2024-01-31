package com.example.mynotes.ui.funciones

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.mynotes.ui.models.Nota
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class Funciones {
    companion object {

        fun crearNota(titulo: EditText, descripcion: EditText, dialog: AlertDialog, reference: DatabaseReference, context: Context
        ) {

            // Obtener el ID del usuario actual
            val user = Firebase.auth.currentUser?.uid

            // Crear un objeto Map para guardar los datos de la nota
            val tituloAsignado = titulo.text.toString()
            val descripcionAsignado = descripcion.text.toString()
            val nota = Nota(reference.push().key, tituloAsignado, descripcionAsignado)

            // Guardar la nota en Firebase
            reference.child(nota.id ?: "").setValue(nota)
                .addOnSuccessListener {
                    // La nota se guardó con éxito
                    Toast.makeText(context, "Nota creada con éxito", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .addOnFailureListener { e ->
                    // Hubo un error al guardar la nota
                    Log.w(ContentValues.TAG, "Error al guardar la nota", e)
                    Toast.makeText(
                        context,
                        "Ha habido un problema al crear la nota",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        fun eliminarNota(context: Context, notaId: String, reference:DatabaseReference) {

            val user = FirebaseAuth.getInstance().currentUser

            if (user != null) {
                val userId = user.uid
                val likesRef = reference.database.getReference("Usuario/${userId}/Notas/${notaId}")

                likesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        try {
                            if (snapshot.exists()) {
                                // El modelo ya está en la lista de "me gusta", eliminarlo
                                likesRef.removeValue();
                                Toast.makeText(
                                    context,
                                    "Nota eliminada con exito",
                                    Toast.LENGTH_SHORT
                                )
                                    .show();
                            } else {
                                Toast.makeText(context, "¿Nota no existe?", Toast.LENGTH_SHORT)
                                    .show();
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error al eliminar nota", Toast.LENGTH_SHORT)
                                .show();
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Manejar error
                    }
                })
            }

        }
        fun eliminarNotaOculta(context: Context, notaId: String, reference:DatabaseReference) {

            val user = FirebaseAuth.getInstance().currentUser

            if (user != null) {
                val userId = user.uid
                val likesRef = reference.database.getReference("Usuario/${userId}/NotasOcultas/${notaId}")

                likesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        try {
                            if (snapshot.exists()) {
                                // El modelo ya está en la lista de "me gusta", eliminarlo
                                likesRef.removeValue();
                                Toast.makeText(
                                    context,
                                    "Nota eliminada con exito",
                                    Toast.LENGTH_SHORT
                                )
                                    .show();
                            } else {
                                Toast.makeText(context, "¿Nota no existe?", Toast.LENGTH_SHORT)
                                    .show();
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error al eliminar nota", Toast.LENGTH_SHORT)
                                .show();
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Manejar error
                    }
                })
            }

        }

        fun editarNota(context: Context, notaId: String, nuevoTitulo: String, nuevaDescripcion: String){

            val user = FirebaseAuth.getInstance().currentUser
            val database = ReferenciasFirebase.referenciaBaseDatos("Usuario/${user!!.uid}/Notas/${notaId}")


            if(user != null){
                val notaRef = database
                notaRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        try {
                            if (snapshot.exists()) {
                                // La nota existe, actualizarla
                                val notaActualizada = Nota(notaId, nuevoTitulo, nuevaDescripcion)
                                notaRef.setValue(notaActualizada)
                                Toast.makeText(context, "Nota actualizada", Toast.LENGTH_SHORT).show();
                            } else {
                                // La nota no existe, manejar el error
                                Toast.makeText(context, "¿La nota no existe?", Toast.LENGTH_SHORT).show();
                            }
                        }catch (e:Exception){
                            Toast.makeText(context, "Error al editar nota", Toast.LENGTH_SHORT).show();
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Manejar error
                    }
                })
            }

        }
        fun editarNotaOculta(context: Context, notaId: String, nuevoTitulo: String, nuevaDescripcion: String){

            val user = FirebaseAuth.getInstance().currentUser
            val database = ReferenciasFirebase.referenciaBaseDatos("Usuario/${user!!.uid}/NotasOcultas/${notaId}")


            if(user != null){
                val notaRef = database
                notaRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        try {
                            if (snapshot.exists()) {
                                // La nota existe, actualizarla
                                val notaActualizada = Nota(notaId, nuevoTitulo, nuevaDescripcion)
                                notaRef.setValue(notaActualizada)
                                Toast.makeText(context, "Nota actualizada", Toast.LENGTH_SHORT).show();
                            } else {
                                // La nota no existe, manejar el error
                                Toast.makeText(context, "¿La nota no existe?", Toast.LENGTH_SHORT).show();
                            }
                        }catch (e:Exception){
                            Toast.makeText(context, "Error al editar nota", Toast.LENGTH_SHORT).show();
                        }


                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Manejar error
                    }
                })
            }

        }

        fun ocultarNota(context: Context, id: String, titulo:String, descripcion:String){

            // Obtener el ID del usuario actual
            val user = Firebase.auth.currentUser?.uid

            // Crear un objeto para guardar los datos de la nota
            val nota = Nota(id, titulo, descripcion)

            // Crear una referencia a la base de datos de Firebase
            val database = FirebaseDatabase.getInstance()
            val reference = ReferenciasFirebase.referenciaBaseDatos("Usuario/${user}/NotasOcultas")

            // Si el usuario es distinto de null guardar la nota en Firebase
            if(user != null){
                reference.child(nota.id!!).setValue(nota)
                    .addOnSuccessListener {
                        // La nota se guardó con éxito
                        Toast.makeText(context, "Nota oculta con éxito", Toast.LENGTH_SHORT).show()
                        eliminarNota(context, id, reference)
                    }
                    .addOnFailureListener { e ->
                        // Hubo un error al guardar la nota
                        Log.w(ContentValues.TAG, "Error al ocultar la nota", e)
                        Toast.makeText(context, "Ha habido un problema al ocultar la nota", Toast.LENGTH_SHORT).show()
                    }
            }else{
                Toast.makeText(context, "Usuario null", Toast.LENGTH_SHORT).show()
            }


        }

        fun DesocultarNota(context: Context, id: String, titulo:String, descripcion:String){

            // Obtener el ID del usuario actual
            val user = Firebase.auth.currentUser?.uid

            // Crear un objeto Map para guardar los datos de la nota

            val nota = Nota(id, titulo, descripcion)

            // Crear una referencia a la base de datos de Firebase
            val reference = ReferenciasFirebase.referenciaBaseDatos("Usuario/${user}/Notas")

            // Guardar la nota en Firebase
            reference.child(nota.id!!).setValue(nota)
                .addOnSuccessListener {
                    // La nota se guardó con éxito
                    Toast.makeText(context, "Nota desoculta con éxito", Toast.LENGTH_SHORT).show()
                    eliminarNotaOculta(context, id, reference)
                }
                .addOnFailureListener { e ->
                    // Hubo un error al guardar la nota
                    Log.w(ContentValues.TAG, "Error al desocultar la nota", e)
                    Toast.makeText(context, "Ha habido un problema al desocultar la nota", Toast.LENGTH_SHORT).show()
                }
        }
    }

}