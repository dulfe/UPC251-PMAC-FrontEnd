package com.betondecken.trackingsystem.ui.register

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.betondecken.trackingsystem.R
import com.betondecken.trackingsystem.databinding.ActivityRegisterBinding
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private val viewModel: RegisterViewModel by viewModels()
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupComponents()
        setupListeners()
        observeState()
        observeEvents()


    }

    private fun setupComponents() {
        // Agregar el toolbar como ActionBar
        //val toolbar = findViewById<MaterialToolbar>(R.id.my_toolbar)
        val toolbar = binding.myToolbar
        setSupportActionBar(toolbar)

        // Estas lineas son para mostrar el icono de la flecha de regreso en la barra de herramientas
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupListeners() {
        binding.txtFirstNames.addTextChangedListener{ text ->
            viewModel.onFirstNamesInputChanged(text.toString())
        }
        binding.txtLastNames.addTextChangedListener{ text ->
            viewModel.onLastNamesInputChanged(text.toString())
        }
        binding.txtEmail.addTextChangedListener{ text ->
            viewModel.onEmailInputChanged(text.toString())
        }
        binding.txtPassword.addTextChangedListener{ text ->
            viewModel.onPasswordInputChanged(text.toString())
        }
        binding.txtConfirmPassword.addTextChangedListener{ text ->
            viewModel.onConfirmPasswordInputChanged(text.toString())
        }

        binding.btnSubmit.setOnClickListener{
            viewModel.onSubmitClick()
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.txtFirstNames.isEnabled = !state.isSaving
                    binding.txtLastNames.isEnabled = !state.isSaving
                    binding.txtEmail.isEnabled = !state.isSaving
                    binding.txtPassword.isEnabled = !state.isSaving
                    binding.txtConfirmPassword.isEnabled = !state.isSaving

                    binding.btnSubmit.visibility = if (state.isSaving) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }

                    binding.progressBar.visibility = if (state.isSaving) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is RegisterEvent.Error -> {
                            Toast.makeText(this@RegisterActivity, event.message, Toast.LENGTH_SHORT).show()
                        }
                        is RegisterEvent.Success -> {
                            Toast.makeText(this@RegisterActivity, "Registro Creado!", Toast.LENGTH_SHORT).show()

                            // Cerrar pantalla
                            finish()
                        }
                    }
                }
            }
        }
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