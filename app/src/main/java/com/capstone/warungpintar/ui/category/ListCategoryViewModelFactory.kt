package com.capstone.warungpintar.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.warungpintar.data.repository.ProductRepositoryOld
import com.capstone.warungpintar.di.Injection

class ListCategoryViewModelFactory(private val productRepositoryOld: ProductRepositoryOld) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListCategoryViewModel::class.java)) {
            return ListCategoryViewModel(productRepositoryOld) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ListCategoryViewModelFactory? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ListCategoryViewModelFactory(
                    Injection.provideProductRepository()
                )
            }.also { instance = it }
    }
}