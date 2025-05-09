package com.betondecken.trackingsystem.ui

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.betondecken.trackingsystem.R
import com.betondecken.trackingsystem.support.SessionManager
import com.betondecken.trackingsystem.ui.login.LoginActivity
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.os.Bundle

@AndroidEntryPoint
abstract class BaseMenuActivity : AppCompatActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)

        val emailItem = menu?.findItem(R.id.mnuUser)
        emailItem?.title = sessionManager.getUser()?.email ?: "Usuario Desconocido"

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnuLogout -> {
                sessionManager.clearSession()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Cierra la actividad actual para que el usuario no pueda volver atrás sin loguearse
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    protected fun setupActionBar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)
    }
}