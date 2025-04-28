package com.betondecken.trackingsystem.ui.tracking

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.betondecken.trackingsystem.R
import com.betondecken.trackingsystem.entities.ResumenDeOrdenResponse
import com.betondecken.trackingsystem.repositories.GetOrdersResult
import com.betondecken.trackingsystem.repositories.TrackingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TrackingUiState(
    val trackingCode: String = "",
    val isLoadingRecentSearches: Boolean = false,
    val recentSearchedOrders: List<ResumenDeOrdenResponse> = emptyList(),
)

sealed class TrackingEvent {
    data object IsValid: TrackingEvent()
    data class Error(val message: String) : TrackingEvent()
}

@HiltViewModel
class TrackingViewModel @Inject constructor  (
    private val application: Application,
    private val trackingRepository: TrackingRepository,
) : AndroidViewModel(application) {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is TRACKING Fragment"
//    }
//    val text: LiveData<String> = _text

    private val _uiState = MutableStateFlow(TrackingUiState())
    val uiState: StateFlow<TrackingUiState> = _uiState

    private val _events = MutableSharedFlow<TrackingEvent>()
    val events: SharedFlow<TrackingEvent> = _events.asSharedFlow()

    fun onTrackingCodeInputChanged(input: String) {
        _uiState.update { it.copy(trackingCode = input) }
    }

    fun onSearchClick() {
        val currentTrackingCode = _uiState.value.trackingCode

        // Validación básica (ej. que no estén vacíos)
        if (currentTrackingCode.isEmpty()) {
            viewModelScope.launch {
                _events.emit(TrackingEvent.Error(application.getString(R.string.tracking_validation_enter_trackingcode)))
            }
            return // Salir de la función si la validación falla
        }

        // Lanzar una coroutine
        viewModelScope.launch {
            _events.emit(TrackingEvent.IsValid)
        }
    }

    fun loadRecentSearches() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingRecentSearches = true) }
            val recentSearches = trackingRepository.getRecentOrders(10)
            when (recentSearches) {
                is GetOrdersResult.Error -> {
                    _events.emit(TrackingEvent.Error(recentSearches.message))
                }
                is GetOrdersResult.Success -> {
                    _uiState.update { it.copy(recentSearchedOrders = recentSearches.orders) }
                }
            }
            _uiState.update { it.copy(isLoadingRecentSearches = false) }
        }
    }
}