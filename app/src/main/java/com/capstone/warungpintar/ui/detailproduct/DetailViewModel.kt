package com.capstone.warungpintar.ui.detailproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.model.Product
import com.capstone.warungpintar.data.repository.ProductRepositoryOld
import kotlinx.coroutines.launch

class DetailViewModel(private val productRepositoryOld: ProductRepositoryOld) : ViewModel() {

    private var _resultDetail: MutableLiveData<ResultState<Product>> = MutableLiveData()
    val resultDetail: LiveData<ResultState<Product>> get() = _resultDetail

    fun getDetailProductById(productId: Int) {
        viewModelScope.launch {
            productRepositoryOld.getDetailProduct(productId).collect { result ->
                _resultDetail.value = result
            }
        }
    }
}