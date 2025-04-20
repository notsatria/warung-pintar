package com.capstone.warungpintar.ui.deleteproduct

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.local.entities.ProductEntity
import com.capstone.warungpintar.data.local.entities.ProductOutEntity
import com.capstone.warungpintar.data.repository.ProductRepository
import com.capstone.warungpintar.utils.toMillis
import com.capstone.warungpintar.worker.InstanceWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DeleteProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) :
    ViewModel() {

    var productList = emptyList<ProductEntity>()
    var selectedProduct: ProductEntity? = null

    val productOutState: MutableLiveData<ResultState<String>> = MutableLiveData()

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            productRepository.getProducts().collect { result ->
                productList = result
            }
        }
    }

    fun insertProductOut(tglKeluar: String, jumlah: Int, hargaJual: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            productOutState.postValue(ResultState.Loading)
            try {
                if (selectedProduct != null) {
                    val productOut = ProductOutEntity(
                        barangId = selectedProduct!!.id,
                        tanggalKeluar = tglKeluar.toMillis(),
                        jumlah = jumlah,
                        hargaJual = hargaJual
                    )
                    val res = productRepository.insertProductOut(productOut)

                    productRepository.updateProduct(
                        selectedProduct!!.copy(jumlah = selectedProduct!!.jumlah - jumlah),
                        isOnProductOut = true,
                        tglKeluar
                    )

                    if (res > 0) {
                        productOutState.postValue(
                            ResultState.Success("Berhasil menambah barang keluar")
                        )
                    } else {
                        productOutState.postValue(ResultState.Error("Gagal menambah barang keluar"))
                    }
                }
            } catch (e: Exception) {
                Timber.e("Error on insertProductOut: ${e.message}")
                productOutState.postValue(ResultState.Error(e.message.toString()))
            }
        }
    }
}