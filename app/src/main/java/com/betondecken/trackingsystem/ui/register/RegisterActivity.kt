package com.betondecken.trackingsystem.ui.register

import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.betondecken.trackingsystem.R
import com.google.android.material.appbar.MaterialToolbar

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Agregar el toolbar como ActionBar
        val toolbar = findViewById<MaterialToolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        // Estas lineas son para mostrar el icono de la flecha de regreso en la barra de herramientas
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.top_app_bar, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
    // Handle the Up button click
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed() // Navigate back using the back dispatcher
        return true
    }
}