package net.sistransito.mobile.ui.ait.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.sistransito.mobile.data.local.entity.CityEntity
import net.sistransito.mobile.data.repository.CityRepository
import net.sistransito.mobile.ait.AitData
import net.sistransito.mobile.ait.AitObject
import javax.inject.Inject

@HiltViewModel
class AitAddressViewModel @Inject constructor(
    private val cityRepository: CityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AitAddressUiState>(AitAddressUiState.Initial)
    val uiState: StateFlow<AitAddressUiState> = _uiState.asStateFlow()

    private val _cities = MutableStateFlow<List<CityEntity>>(emptyList())
    val cities: StateFlow<List<CityEntity>> = _cities.asStateFlow()

    private val aitData: AitData = AitObject.getAitData()

    init {
        loadCities()
    }

    private fun loadCities() {
        viewModelScope.launch {
            cityRepository.getAllCities()
                .catch { e ->
                    _uiState.value = AitAddressUiState.Error(e.message ?: "Erro ao carregar cidades")
                }
                .collect { cities ->
                    _cities.value = cities
                    _uiState.value = AitAddressUiState.Success
                }
        }
    }

    fun searchCities(query: String) {
        viewModelScope.launch {
            cityRepository.searchCities(query)
                .catch { e ->
                    _uiState.value = AitAddressUiState.Error(e.message ?: "Erro ao buscar cidades")
                }
                .collect { cities ->
                    _cities.value = cities
                }
        }
    }

    fun selectCity(city: CityEntity) {
        aitData.apply {
            this.city = city.name
            this.cityCode = city.code
            this.state = city.state
        }
        _uiState.value = AitAddressUiState.CitySelected(city)
    }

    fun updateAddress(address: String) {
        aitData.address = address
    }

    fun updateDateTime(date: String, time: String) {
        aitData.apply {
            this.aitDate = date
            this.aitTime = time
        }
    }

    fun validateInputs(): Boolean {
        return aitData.run {
            !address.isNullOrBlank() &&
            !city.isNullOrBlank() &&
            !aitDate.isNullOrBlank() &&
            !aitTime.isNullOrBlank()
        }
    }
}

sealed class AitAddressUiState {
    object Initial : AitAddressUiState()
    object Success : AitAddressUiState()
    data class Error(val message: String) : AitAddressUiState()
    data class CitySelected(val city: CityEntity) : AitAddressUiState()
} 