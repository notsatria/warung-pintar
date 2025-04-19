package com.capstone.warungpintar.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.local.entities.UserEntity
import com.capstone.warungpintar.data.remote.model.response.LoginResponse
import com.capstone.warungpintar.data.repository.UserRepository
import com.capstone.warungpintar.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    val loginResult = MutableLiveData<ResultState<UserEntity>>()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginResult.value = ResultState.Loading
            val user = userRepository.login(email, password)
            if (user != null) {
                loginResult.value = ResultState.Success(user)
                userPreferences.setUserLoggedIn(true)
                userPreferences.setUserEmail(email)
            } else {
                loginResult.value = ResultState.Error("Login gagal, pengguna tidak ditemukan")
            }
        }
    }
}