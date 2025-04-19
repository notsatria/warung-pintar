package com.capstone.warungpintar.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.repository.ProductRepositoryOld
import kotlinx.coroutines.launch

class ListCategoryViewModel(private val productRepositoryOld: ProductRepositoryOld) : ViewModel() {

    private var _listCategory: MutableLiveData<ResultState<List<String>>> = MutableLiveData()
    val listCategory: LiveData<ResultState<List<String>>> get() = _listCategory

    fun getListCategory() {
        viewModelScope.launch {
            productRepositoryOld.getListCategoryProduct().collect { result ->
                _listCategory.value = result
            }
        }
    }
}