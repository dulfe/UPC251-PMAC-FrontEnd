package com.betondecken.trackingsystem.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.betondecken.trackingsystem.HomeActivity
import com.betondecken.trackingsystem.R
import com.betondecken.trackingsystem.ui.register.RegisterActivity
import com.google.android.material.elevation.SurfaceColors

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val registerButton: View = findViewById(R.id.btnRegister)
        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        val forgotPasswordButton: View = findViewById(R.id.btnForgotPassword)
        forgotPasswordButton.setOnClickListener {
            Toast.makeText(this, "Un link para cambiar su password se ha enviado a su email.", Toast.LENGTH_SHORT).show()
        }

        val loginButton: View = findViewById(R.id.btnLogin)
        loginButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

}