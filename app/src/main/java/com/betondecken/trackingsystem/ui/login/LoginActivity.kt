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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.betondecken.trackingsystem.HomeActivity
import com.betondecken.trackingsystem.MyApplication
import com.betondecken.trackingsystem.R
import com.betondecken.trackingsystem.databinding.ActivityLoginBinding
import com.betondecken.trackingsystem.databinding.FragmentTrackingBinding
import com.betondecken.trackingsystem.repositories.UserLoginResult
import com.betondecken.trackingsystem.repositories.UserRepository
import com.betondecken.trackingsystem.ui.register.RegisterActivity

class LoginViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels {
        val userRepository = (application as MyApplication).userRepository
        LoginViewModelFactory(userRepository)
    }

    //private lateinit var loadingIndicator: CircularProgressIndicator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflar y asignar el diseño de la actividad
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        // Obtener referencia al indicador de progreso (si existe en tu layout activity_login.xml)
//        loadingIndicator = binding.root.findViewById(R.id.loading_indicator)


        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loginViewModel.isLoading.observe(this) { isLoading ->
            //loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !isLoading
            binding.btnRegister.isEnabled = !isLoading
            binding.btnForgotPassword.isEnabled = !isLoading
            binding.txtEmail.isEnabled = !isLoading
            binding.txtPassword.isEnabled = !isLoading
        }

        loginViewModel.loginResult.observe(this) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is UserLoginResult.Success -> {
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is UserLoginResult.Error -> {
                        Toast.makeText(this, "Login Failed: ${result.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        loginViewModel.errorMessage.observe(this) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        // --- Conectar acciones de la UI a métodos del ViewModel (sin cambios aquí) ---


        binding.txtEmail.addTextChangedListener { text ->
            loginViewModel.onEmailInputChanged(text.toString())
        }

        binding.txtPassword.addTextChangedListener { text ->
            loginViewModel.onPasswordInputChanged(text.toString())
        }

        // Enlazar los eventos
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnForgotPassword.setOnClickListener {
            Toast.makeText(this, "Un link para cambiar su password se ha enviado a su email.", Toast.LENGTH_SHORT).show()
        }

        binding.btnLogin.setOnClickListener {
            //startActivity(Intent(this, HomeActivity::class.java))
            loginViewModel.onLoginClick()
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}