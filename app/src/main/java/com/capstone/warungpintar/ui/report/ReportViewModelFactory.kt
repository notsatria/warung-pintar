package com.capstone.warungpintar.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.warungpintar.data.repository.ProductRepositoryOld
import com.capstone.warungpintar.di.Injection

class ReportViewModelFactory(private val productRepositoryOld: ProductRepositoryOld) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            return ReportViewModel(productRepositoryOld) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ReportViewModelFactory? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ReportViewModelFactory(
                    Injection.provideProductRepository()
                )
            }.also { instance = it }
    }
}