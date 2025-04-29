package com.betondecken.trackingsystem.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.betondecken.trackingsystem.R
import com.betondecken.trackingsystem.entities.UsuarioResponse
import com.betondecken.trackingsystem.repositories.UserLoginResult
import com.betondecken.trackingsystem.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    //val loginResult: UserLoginResult? = null,
    //val errorMessage: String? = null
)

sealed class LoginEvent {
    data class Error(val message: String) : LoginEvent()
    data class Success(val result: UsuarioResponse) : LoginEvent()
}

// ViewModel para la pantalla de Login
@HiltViewModel
class LoginViewModel @Inject constructor (
    private val application: Application,
    private val userRepository: UserRepository
//) : ViewModel() {
): AndroidViewModel(application) {

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
                _events.emit(LoginEvent.Error(application.getString(R.string.login_validation_enter_email_and_password)))
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
                _events.emit(LoginEvent.Error(e.message ?: application.getString(R.string.unknown_error)))
            } finally {
                // Ocultar el indicador de carga
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

}