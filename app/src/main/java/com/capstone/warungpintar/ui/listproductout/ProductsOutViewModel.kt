package com.capstone.warungpintar.ui.listproductout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.repository.ProductRepositoryOld
import kotlinx.coroutines.launch

class ProductsOutViewModel(private val productRepositoryOld: ProductRepositoryOld) : ViewModel() {

    private var _listProductOut: MutableLiveData<ResultState<List<String>>> = MutableLiveData()
    val listProductOut: LiveData<ResultState<List<String>>> get() = _listProductOut

    fun getListProductOut(email: String) {
        viewModelScope.launch {
            productRepositoryOld.getListProductOut(email).collect { result ->
                _listProductOut.value = result
            }
        }
    }
}