package com.capstone.warungpintar.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.local.entities.UserEntity
import com.capstone.warungpintar.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    val registerResult = MutableLiveData<ResultState<String>>()

    fun register(namaWarung: String, nomorTelp: String, email: String, password: String) {
        val user = UserEntity(
            namaWarung = namaWarung,
            noTelp = nomorTelp,
            email = email,
            password = password
        )
        viewModelScope.launch {
            try {
                registerResult.value = ResultState.Loading
                val res = userRepository.register(user)
                if (res > 0) {
                    registerResult.value = ResultState.Success("Registrasi berhasil")
                }
            } catch (e: Exception) {
                registerResult.value = ResultState.Error("Registrasi gagal: ${e.message}")
            }
        }
    }
}