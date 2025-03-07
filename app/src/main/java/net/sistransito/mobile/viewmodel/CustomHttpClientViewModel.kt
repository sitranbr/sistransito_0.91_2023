package net.sistransito.mobile.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import net.sistransito.mobile.plate.data.DataFromPlate
import net.sistransito.mobile.http.repository.CustomHttpClientRepository

class CustomHttpClientViewModel(application: Application) : AndroidViewModel(application) {

    private val _response = MutableLiveData<DataFromPlate?>()
    val response: LiveData<DataFromPlate?> = _response

    private val _hasError = MutableLiveData<Boolean>()
    val hasError: LiveData<Boolean> = _hasError

    private val repository: CustomHttpClientRepository = CustomHttpClientRepository.create()
    private val gson = Gson()

    fun searchPlate(plate: String, type: String) {
        Log.d(TAG, "Searching plate: $plate, type: $type")
        viewModelScope.launch {
            try {
                val result = repository.searchPlate(plate, type)
                _response.value = result
                _hasError.value = result == null
                val jsonResult = gson.toJson(result)
                Log.d(TAG, "Search result JSON: $jsonResult")
            } catch (e: Exception) {
                Log.e(TAG, "Error searching plate", e)
                _response.value = null
                _hasError.value = true
            }
        }
    }

    companion object {
        private const val TAG = "CustomHttpClientVM"
    }
}
