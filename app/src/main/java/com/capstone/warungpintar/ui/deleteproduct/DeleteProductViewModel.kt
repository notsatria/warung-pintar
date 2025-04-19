package com.capstone.warungpintar.ui.deleteproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.remote.model.request.DeleteProductRequest
import com.capstone.warungpintar.data.repository.ProductRepositoryOld
import kotlinx.coroutines.launch

class DeleteProductViewModel(private val productRepositoryOld: ProductRepositoryOld) : ViewModel() {

    private var _resultDelete: MutableLiveData<ResultState<String>> = MutableLiveData()
    val resultDelete: LiveData<ResultState<String>> get() = _resultDelete

    fun deleteProduct(email: String, productName: String, data: DeleteProductRequest) {
        viewModelScope.launch {
            productRepositoryOld.deleteProduct(email, productName, data).collect { result ->
                _resultDelete.value = result
            }
        }
    }
}