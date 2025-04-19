package com.capstone.warungpintar.ui.report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    var listReport: MutableLiveData<ResultState<List<ReportItem>>> = MutableLiveData()

    init {
        getListReport()
    }

    private fun getListReport() {
        viewModelScope.launch {
            try {
                listReport.value = ResultState.Loading
                productRepository.getReportList().collect {
                    listReport.value = ResultState.Success(it)
                }
            } catch (e: Exception) {
                listReport.value = ResultState.Error(e.message.toString())
            }
        }
    }
}