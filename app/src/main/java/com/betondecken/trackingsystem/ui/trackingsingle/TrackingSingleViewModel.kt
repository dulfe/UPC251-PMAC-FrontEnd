package com.betondecken.trackingsystem.ui.trackingsingle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.betondecken.trackingsystem.entities.DetalleDeOrdenResponse
import com.betondecken.trackingsystem.entities.RepositoryResult
import com.betondecken.trackingsystem.repositories.GetOrderByTrackingCodeResult
import com.betondecken.trackingsystem.repositories.TrackingRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class TrackingSingleUiState(
    val trackingCode: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String? = null,
    val trackingDetails: DetalleDeOrdenResponse? = null,
    val isSimulationActive: Boolean = false,
    val currentLocation: LatLng? = null
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
                    trackingCode = trackingCode,
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

    fun startSimulation() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isSimulationActive = true)
            }

            while (_uiState.value.isSimulationActive) {
                val location = withContext(Dispatchers.IO) {
                    trackingRepository.getCurrentLocation(_uiState.value.trackingCode)
                }

                if (location is RepositoryResult.Success) {
                    val latLng = LatLng(location.data.latitud, location.data.longitud)

                    _uiState.update {
                        it.copy(currentLocation = latLng)
                    }
                }

                delay(1000)
            }
        }
    }

    fun stopSimulation() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isSimulationActive = false)
            }
        }
    }
}