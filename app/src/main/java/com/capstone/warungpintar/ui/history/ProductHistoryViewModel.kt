package com.capstone.warungpintar.ui.history

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
class ProductHistoryViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    var listHistory: MutableLiveData<ResultState<List<HistoryItem>>> =
        MutableLiveData()

    init {
        getListHistory()
    }

    private fun getListHistory() {
        viewModelScope.launch {
            try {
                listHistory.value = ResultState.Loading
                productRepository.getHistoryList().collect { result ->
                    listHistory.value = ResultState.Success(result)
                }
            } catch (e: Exception) {
                listHistory.value = ResultState.Error(e.message.toString())
                Timber.d("Error on getListHistory: ${e.message}")
            }
        }
    }
}