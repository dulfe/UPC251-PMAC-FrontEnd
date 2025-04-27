package com.betondecken.trackingsystem

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.betondecken.trackingsystem.ui.login.LoginActivity
import com.betondecken.trackingsystem.ui.trackingsingle.TrackingSingleActivity

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_launcher)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // get UserRepository from MyApplication
        val userRepository = (application as MyApplication).userRepository


        // start LoginActivity
        startActivity(Intent(this, LoginActivity::class.java))

        // Nota: Esta clase tal vez tiene la intension que el usuario actual se almacene en algun
        // lugar que sobreviva a que se cierre la aplicacion, pero eso tomara tiempo y tal vez
        // no valga la pena. Por ahora solo se inicia la actividad de login y se cierra esta.

        // finish LauncherActivity
        finish()
    }
}