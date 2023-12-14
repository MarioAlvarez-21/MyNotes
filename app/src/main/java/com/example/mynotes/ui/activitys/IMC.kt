package com.example.mynotes.ui.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mynotes.R
import com.example.mynotes.databinding.ActivityImcBinding
import java.text.DecimalFormat

class IMC : AppCompatActivity() {

    private lateinit var binding : ActivityImcBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listener()
        volver()
    }

    fun listener() {
        binding.cardView2.isPressed = true

        binding.cardView.setOnClickListener {
            binding.cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.presionado))
            binding.cardView2.setCardBackgroundColor(ContextCompat.getColor(this, R.color.FondoApp))
        }

        binding.cardView2.setOnClickListener {
            binding.cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.FondoApp))
            binding.cardView2.setCardBackgroundColor(ContextCompat.getColor(this, R.color.presionado))
        }
        binding.BTCalcularIMC.setOnClickListener(){
            calcularIMC()
        }
    }

    private fun calcularIMC(){
        val alturastr = binding.ETAlturaIMC.editText!!.text.toString()
        val pesostr = binding.ETPesoIMC.editText!!.text.toString()
        val edadstr = binding.ETEdadIMC.editText!!.text.toString()


        if(alturastr.isEmpty() || pesostr.isEmpty() || edadstr.isEmpty()){
            Toast.makeText(this, "No puedes dejar ningun campo vacio", Toast.LENGTH_SHORT).show()
        } else{
            var altura = alturastr.toDouble()
            var peso = pesostr.toDouble()
            var resultado = peso / (altura / 100 * altura / 100)
            var formatoResultado = DecimalFormat("#.##").format(resultado)

            binding.TVImcIMC.text = formatoResultado.toString()
            if(resultado < 18.5){
                binding.TVResultadoIMCIMC.text = "Si tu IMC es ${formatoResultado} estas por debajo de tu peso ideal"
            }else if(resultado in 18.5..24.9){
                binding.TVResultadoIMCIMC.text = "Si tu IMC es ${formatoResultado} estas en tu peso ideal"
            }else if(resultado in 25.0 .. 29.9){
                binding.TVResultadoIMCIMC.text = "Si tu IMC es ${formatoResultado} por encima de tu peso ideal"
            }else if(resultado >= 30.0){
                binding.TVResultadoIMCIMC.text = "Si tu IMC es ${formatoResultado} tienes un problema de obesidad"
            }
        }


    }

    private fun volver(){
        binding.volverIMC.setOnClickListener(){
            finish()
        }
    }

}