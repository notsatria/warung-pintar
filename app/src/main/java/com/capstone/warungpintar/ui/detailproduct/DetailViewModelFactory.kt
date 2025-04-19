package com.capstone.warungpintar.ui.detailproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.warungpintar.data.repository.ProductRepositoryOld
import com.capstone.warungpintar.di.Injection

class DetailViewModelFactory(private val productRepositoryOld: ProductRepositoryOld) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(productRepositoryOld) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: DetailViewModelFactory? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: DetailViewModelFactory(
                    Injection.provideProductRepository()
                )
            }.also { instance = it }
    }
}