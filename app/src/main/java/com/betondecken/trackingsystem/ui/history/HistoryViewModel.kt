package com.betondecken.trackingsystem.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betondecken.trackingsystem.entities.DetalleDeOrdenResponse
import com.betondecken.trackingsystem.entities.RepositoryResult
import com.betondecken.trackingsystem.entities.ResumenDeOrdenResponse
import com.betondecken.trackingsystem.repositories.TrackingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.YearMonth
import javax.inject.Inject

data class MapData(
    val year: Int,
    val month: Int,
    val label: String,
    val value: Int,
)
data class HistoryUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val message: String? = null,
    val history: List<MapData>? = null,
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val trackingRepository: TrackingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    fun loadHistory() {
        viewModelScope.launch {
            // Se inicializa el estado de la UI
            _uiState.value = HistoryUiState(
                isLoading = true,
                isError = false,
                message = null,
                history = null
            )

            val data = trackingRepository.getRecentOrders(9999)
            when (data) {
                is RepositoryResult.Success -> {
                    val groupedData = groupByMonth(data.data)
                    _uiState.value = HistoryUiState(
                        isLoading = false,
                        isError = false,
                        message = null,
                        history = groupedData
                    )
                }
                is RepositoryResult.Error -> {
                    _uiState.value = HistoryUiState(
                        isLoading = false,
                        isError = true,
                        message = data.error,
                        history = null
                    )
                }
            }
        }
    }

    fun groupByMonth(history: List<ResumenDeOrdenResponse>): List<MapData> {
        val today = OffsetDateTime.now()
        val last12Months = (0..11)
            .map { today.minusMonths(it.toLong()) }
            .map { YearMonth.of(it.year, it.monthValue) }
            .associateWith { 0 } // Initialize each month with a count of 0

        val groupedHistory = history
            .filter { it.fechaDeEntrega != null && !it.fechaDeEntrega.isBefore(today.minusMonths(12)) }
            .groupBy { YearMonth.of(it.fechaDeEntrega!!.year, it.fechaDeEntrega.monthValue) }
            .mapValues { it.value.size }

        val result = last12Months
            .map { (yearMonth, initialValue) ->
            MapData(
                year = yearMonth.year,
                month = yearMonth.monthValue,
                label = yearMonth.month.name,
                value = groupedHistory[yearMonth] ?: initialValue
            )
        }

        val sortedResult = result
            .sortedBy { (it.year * 100) + it.month }

        return sortedResult
    }

}