package com.capstone.warungpintar.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.warungpintar.data.repository.ProductRepositoryOld
import com.capstone.warungpintar.di.Injection

class ProductHistoryViewModelFactory(private val productRepositoryOld: ProductRepositoryOld) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductHistoryViewModel::class.java)) {
            return ProductHistoryViewModel(productRepositoryOld) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ProductHistoryViewModelFactory? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ProductHistoryViewModelFactory(
                    Injection.provideProductRepository()
                )
            }.also { instance = it }
    }
}