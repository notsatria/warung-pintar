package com.capstone.warungpintar.ui.liststockproduct

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.model.Product
import com.capstone.warungpintar.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListStockViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    val resultList = MutableLiveData<ResultState<List<Product>>>()

    fun getListProduct() {
        viewModelScope.launch {
            try {
                resultList.value = ResultState.Loading
                productRepository.getAllProductWithCategory().collect { result ->
                    resultList.value = ResultState.Success(result.map { it.toProduct() })
                }
            } catch (e: Exception) {
                resultList.value = ResultState.Error(e.message.toString())
            }
        }
    }
}