package com.example.mynotes.ui.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import com.example.mynotes.R

private lateinit var botonMain: ImageButton


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fullScreen()

        botonMain = findViewById(R.id.botonMain)
    }

    fun fullScreen(){
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    fun navegarLogin(view : View){
        var intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}