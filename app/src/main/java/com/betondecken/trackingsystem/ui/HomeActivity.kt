package com.betondecken.trackingsystem.ui

import android.os.Bundle
import android.view.Menu
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.betondecken.trackingsystem.R
import com.betondecken.trackingsystem.databinding.ActivityHomeBinding
import com.betondecken.trackingsystem.support.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import com.betondecken.trackingsystem.ui.BaseMenuActivity
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseMenuActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupComponents()
        setupListeners()
        observeState()
        observeEvents()
    }

    private fun observeEvents() {
        // Nada por ahora
    }

    private fun observeState() {
        // Nada por ahora
    }

    private fun setupListeners() {
        // Nada por ahora
    }

    private fun setupComponents() {
        setupActionBar()

        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragmentActivityHome.id) as NavHostFragment
        val navController = navHostFragment.navController

        // Esto controlla el comportamiento del ActionBar al cambiar de fragmentos
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_tracking, R.id.navigation_history, R.id.navigation_support
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Esto configura el BottomNavigationView con el NavController y permite la navegación
        navView.setupWithNavController(navController)
    }
}