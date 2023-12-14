package com.example.mynotes.ui.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Nota(var id:String?,
                var titulo: String?,
                var descripcion: String?,
                var fecha:String = obtenerFechaActual(),
                var expand: Boolean = false, ) {

    // Constructor vacío requerido para Firebase
    constructor() : this(null, "", "")
    companion object {
        // Función estática para obtener la fecha actual en un formato específico
        private fun obtenerFechaActual(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return sdf.format(Date())
        }
    }

    override fun toString(): String {
        return "Nota(titulo='$titulo', descripcion='$descripcion')"
    }

}