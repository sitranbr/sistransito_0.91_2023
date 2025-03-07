package net.sistransito.mobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sistransito.mobile.util.User
import net.sistransito.mobile.http.repository.LoginRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _loginResponse = MutableLiveData<User?>()
    val loginResponse: LiveData<User?> = _loginResponse

    private val _hasError = MutableLiveData<Boolean>()
    val hasError: LiveData<Boolean> = _hasError

    private val repository: LoginRepository = LoginRepository.create()

    fun login(email: String, password: String) {
        //Log.d(TAG, "Logging in with email: $email")
        viewModelScope.launch {
            try {
                val result = repository.login(email, password)
                _loginResponse.value = result
                _hasError.value = result == null || result.success != 1
                //Log.d(TAG, "Login result: $result")
            } catch (e: Exception) {
                //Log.e(TAG, "Error during login", e)
                _loginResponse.value = null
                _hasError.value = true
            }
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
