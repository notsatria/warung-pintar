package com.capstone.warungpintar.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListCategoryViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    val categoryNameList: MutableLiveData<ResultState<List<String>>> = MutableLiveData()

    init {
        getListCategory()
    }

    private fun getListCategory() {
        viewModelScope.launch {
            try {
                categoryNameList.value = ResultState.Loading
                productRepository.getCategories().collect { result ->
                    categoryNameList.value = ResultState.Success(result.map { it.namaKategori })
                }
            } catch (e: Exception) {
                categoryNameList.value = ResultState.Error(e.message.toString())
                Timber.e("Error on getListCategory: ${e.message}")
            }
        }
    }
}