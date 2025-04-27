package com.betondecken.trackingsystem.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.betondecken.trackingsystem.HomeActivity
import com.betondecken.trackingsystem.MyApplication
import com.betondecken.trackingsystem.databinding.ActivityLoginBinding
import com.betondecken.trackingsystem.repositories.UserRepository
import com.betondecken.trackingsystem.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflar y asignar el diseño de la actividad
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
        observeState()
        observeEvents()
    }

    private fun setupListeners() {
        // Campos
        binding.txtEmail.addTextChangedListener { text ->
            viewModel.onEmailInputChanged(text.toString())
        }

        binding.txtPassword.addTextChangedListener { text ->
            viewModel.onPasswordInputChanged(text.toString())
        }

        // Botones
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnForgotPassword.setOnClickListener {
            Toast.makeText(this, "Un link para cambiar su password se ha enviado a su email.", Toast.LENGTH_SHORT).show()
        }

        binding.btnLogin.setOnClickListener {
            viewModel.onLoginClick()
        }
    }

    private fun observeState() {
        // Pattern estandar para observar el estado de la UI cuando se usa corutinas
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.btnLogin.isEnabled = !state.isLoading
                    binding.txtEmail.isEnabled = !state.isLoading
                    binding.txtPassword.isEnabled = !state.isLoading
                    binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun observeEvents() {
        // Pattern estandar para observar el estado de los eventos cuando se usa corutinas
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is LoginEvent.Error -> {
                            Toast.makeText(this@LoginActivity, event.message, Toast.LENGTH_SHORT).show()
                        }
                        is LoginEvent.Success -> {
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}