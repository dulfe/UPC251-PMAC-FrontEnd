package com.betondecken.trackingsystem.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betondecken.trackingsystem.entities.Usuario
import com.betondecken.trackingsystem.repositories.UserLoginResult
import com.betondecken.trackingsystem.repositories.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    //val loginResult: UserLoginResult? = null,
    //val errorMessage: String? = null
)

sealed class LoginEvent {
    data class Error(val message: String) : LoginEvent()
    data class Success(val result: Usuario) : LoginEvent()
}

// ViewModel para la pantalla de Login
class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _events = MutableSharedFlow<LoginEvent>()
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    fun onEmailInputChanged(input: String) {
        _uiState.update { it.copy(email = input) }
    }

    fun onPasswordInputChanged(input: String) {
        _uiState.update { it.copy(password = input) }
    }

    fun onLoginClick() {
        val currentEmail = _uiState.value.email
        val currentPassword = _uiState.value.password

        // Validación básica (ej. que no estén vacíos)
        if (currentEmail.isEmpty() || currentPassword.isEmpty()) {
            viewModelScope.launch {
                _events.emit(LoginEvent.Error("Please enter both email and password"))
            }
            return // Salir de la función si la validación falla
        }

        // Indicar que el login está en progreso
        _uiState.update { it.copy(isLoading = true) }

        // Lanzar una coroutine
        viewModelScope.launch {
            try {// Llamar al método login del Repositorio
                val result: UserLoginResult = userRepository.login(currentEmail, currentPassword)

                // Manejar el resultado del Repositorio
                when (result) {
                    is UserLoginResult.Success -> {
                        // Emitir el resultado del Repositorio al flujo de eventos
                        _events.emit(LoginEvent.Success(result.user))
                    }
                    is UserLoginResult.Error -> {
                        // Emitir el error al flujo de eventos
                        _events.emit(LoginEvent.Error(result.message))
                    }
                }
            } catch (e: Exception) {
                _events.emit(LoginEvent.Error(e.message ?: "Unknown error"))
            } finally {
                // Ocultar el indicador de carga
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
//
//    // LiveData para el estado de la UI (email, password, carga)
//    private val _email = MutableLiveData<String>()
//    val email: LiveData<String> = _email
//
//    private val _password = MutableLiveData<String>()
//    val password: LiveData<String> = _password
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    // LiveData para manejar eventos de resultado de login (éxito o error del Repositorio)
//    // Cambiamos el tipo a UserLoginResult
//    private val _loginResult = MutableLiveData<Event<UserLoginResult>>()
//    val loginResult: LiveData<Event<UserLoginResult>> = _loginResult
//
//    // LiveData para manejar mensajes de error generales (ej. validación de campos vacíos antes de llamar al repo)
//    private val _errorMessage = MutableLiveData<Event<String>>()
//    val errorMessage: LiveData<Event<String>> = _errorMessage
//
//
//    // --- Métodos para actualizar el estado desde la UI ---
//
//    fun onEmailInputChanged(input: String) {
//        _email.value = input
//    }
//
//    fun onPasswordInputChanged(input: String) {
//        _password.value = input
//    }
//
//    // --- Método para manejar el click del botón Login ---
//
//    fun onLoginClick() {
//        // Obtener los valores actuales
//        val currentEmail = _email.value
//        val currentPassword = _password.value
//
//        // Validación básica (ej. que no estén vacíos)
//        if (currentEmail.isNullOrEmpty() || currentPassword.isNullOrEmpty()) {
//            _errorMessage.value = Event("Please enter both email and password")
//            return // Salir de la función si la validación falla
//        }
//
//        // Indicar que el login está en progreso
//        _isLoading.value = true
//
//        // Lanzar una coroutine
//        viewModelScope.launch {
//            // Llamar al método login del Repositorio
//            val result: UserLoginResult = userRepository.login(currentEmail, currentPassword)
//
//            // Postear el resultado del Repositorio al LiveData del ViewModel
//            _loginResult.value = Event(result)
//
//            // Ocultar el indicador de carga
//            _isLoading.value = false
//        }
//    }
//
//    // --- Otros métodos si los hubiera ---
}