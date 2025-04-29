package com.betondecken.trackingsystem.ui.trackingsingle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.betondecken.trackingsystem.entities.DetalleDeOrdenResponse
import com.betondecken.trackingsystem.repositories.GetOrderByTrackingCodeResult
import com.betondecken.trackingsystem.repositories.TrackingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TrackingSingleUiState(
    val trackingCode: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String? = null,
    val trackingDetails: DetalleDeOrdenResponse? = null,
)


@HiltViewModel
class TrackingSingleViewModel @Inject constructor(
    private val application: Application,
    private val trackingRepository: TrackingRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(TrackingSingleUiState())
    val uiState: StateFlow<TrackingSingleUiState> = _uiState


    fun loadTrackingDetails(trackingCode: String) {
        viewModelScope.launch {
            // Se inicializa el estado de la UI
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isError = false,
                    message = null,
                    trackingDetails = null
                )
            }

            when (val result = trackingRepository.getOrderByTrackingCode(trackingCode)) {
                is GetOrderByTrackingCodeResult.Success -> {
                    _uiState.update {
                        it.copy(
                            trackingDetails = result.data,
                            isLoading = false
                        )
                    }
                }
                is GetOrderByTrackingCodeResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            message = result.message
                        )
                    }
                }
                is GetOrderByTrackingCodeResult.NotFound -> {
                    _uiState.update {
                        it.copy(
                            isError = true,
                            isLoading = false,
                            message = application.getString(com.betondecken.trackingsystem.R.string.trackingsingle_not_found)
                        )
                    }
                }
            }
        }
    }

}