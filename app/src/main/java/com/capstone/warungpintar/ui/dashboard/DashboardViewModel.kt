package com.capstone.warungpintar.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.warungpintar.data.repository.ProductRepository
import com.capstone.warungpintar.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel
@Inject constructor(
    private val userPreferences: UserPreferences,
    private val productRepository: ProductRepository
) : ViewModel() {

    var userEmail: String = ""

    init {
        getUserEmail()
    }

    private fun getUserEmail() {
        viewModelScope.launch {
            userPreferences.getUserEmail().collect { result ->
                userEmail = result
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.setUserLoggedIn(false)
            userPreferences.setUserEmail("")
        }
    }

    val productLength = productRepository.getProductLength()

    val productInLength = productRepository.getProductInLength()

    val productOutLength = productRepository.getProductOutLength()

    val productsWithLowStockLength = productRepository.getProductsWithLowStockLength()
}