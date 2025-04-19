package com.capstone.warungpintar.ui.listproductout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.warungpintar.data.repository.ProductRepositoryOld
import com.capstone.warungpintar.di.Injection

class ProductsOutViewModelFactory(private val productRepositoryOld: ProductRepositoryOld) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsOutViewModel::class.java)) {
            return ProductsOutViewModel(productRepositoryOld) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ProductsOutViewModelFactory? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ProductsOutViewModelFactory(
                    Injection.provideProductRepository()
                )
            }.also { instance = it }
    }
}