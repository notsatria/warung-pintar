package com.capstone.warungpintar.ui.liststockproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.model.Product
import com.capstone.warungpintar.data.repository.ProductRepositoryOld
import kotlinx.coroutines.launch

class ListStockViewModel(private val productRepositoryOld: ProductRepositoryOld) : ViewModel() {

    private var _resultList: MutableLiveData<ResultState<List<Product>>> = MutableLiveData()
    val resultList: LiveData<ResultState<List<Product>>> get() = _resultList

    fun getListProduct() {
        viewModelScope.launch {
            productRepositoryOld.getAllProduct().collect { result ->
                _resultList.value = result
            }
        }
    }
}