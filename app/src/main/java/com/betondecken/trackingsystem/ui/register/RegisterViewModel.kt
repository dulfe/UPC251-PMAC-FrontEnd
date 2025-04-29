package com.betondecken.trackingsystem.ui.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.betondecken.trackingsystem.entities.NuevoUsuarioInput
import com.betondecken.trackingsystem.entities.UsuarioResponse
import com.betondecken.trackingsystem.entities.RepositoryResult
import com.betondecken.trackingsystem.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    var firstNames: String = "",
    var lastNames: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isSaving: Boolean = false,
    val isError: Boolean = false,
    val message: String? = null,
)

sealed class RegisterEvent {
    data class Error(val message: String) : RegisterEvent()
    data class Success(val user: UsuarioResponse) : RegisterEvent()
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val application: Application,
    private val repository: UserRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    private val _events = MutableSharedFlow<RegisterEvent>()
    val events: SharedFlow<RegisterEvent> = _events.asSharedFlow()

    fun onFirstNamesInputChanged(input: String) {
        _uiState.value = _uiState.value.copy(firstNames = input)
    }

    fun onLastNamesInputChanged(input: String) {
        _uiState.value = _uiState.value.copy(lastNames = input)
    }

    fun onEmailInputChanged(input: String) {
        _uiState.value = _uiState.value.copy(email = input)
    }

    fun onPasswordInputChanged(input: String) {
        _uiState.value = _uiState.value.copy(password = input)
    }

    fun onConfirmPasswordInputChanged(input: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = input)
    }

    fun onSubmitClick() {
        viewModelScope.launch {
            val currentState = _uiState.value

            // Validación básica (ej. que no estén vacíos)
            if (currentState.firstNames.isEmpty() || currentState.lastNames.isEmpty() || currentState.email.isEmpty() || currentState.password.isEmpty() || currentState.confirmPassword.isEmpty()) {
                _events.emit(RegisterEvent.Error("Todos los campos son obligatorios"))
                return@launch
            }
            if (currentState.password != currentState.confirmPassword) {
                _events.emit(RegisterEvent.Error("La contraseña y la confirmación no coinciden"))
                return@launch
            }
            _uiState.value = currentState.copy(isSaving = true)

            // Llamar al repositorio para registrar el usuario
            val newUser: NuevoUsuarioInput = NuevoUsuarioInput(
                nombres = currentState.firstNames,
                apellidos = currentState.lastNames,
                email = currentState.email,
                password = currentState.password,
                confirmacionDePassword = currentState.confirmPassword
            )
            try {
                val result: RepositoryResult<UsuarioResponse> = repository.register(newUser)

                when (result) {
                    is RepositoryResult.Success -> {
                        val user = result.data
                        _events.emit(RegisterEvent.Success(user))
                    }

                    is RepositoryResult.Error -> {
                        _events.emit(RegisterEvent.Error(result.error))
                    }
                }
            } catch (e: Exception) {
                _events.emit(RegisterEvent.Error(e.message ?: "Error desconocido"))
            } finally {
                // Resetear el estado de guardado
                _uiState.value = currentState.copy(isSaving = false)
                if (currentState.isError) {
                    _uiState.value = currentState.copy(isError = false, message = null)
                }
            }
        }
    }
//    private fun emitEvent(event: RegisterEvent) {
//        viewModelScope.launch {
//            _events.emit(event)
//        }
//    }
}