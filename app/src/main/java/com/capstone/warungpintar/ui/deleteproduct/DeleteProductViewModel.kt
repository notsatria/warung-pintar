package com.capstone.warungpintar.ui.deleteproduct

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.local.entities.ProductEntity
import com.capstone.warungpintar.data.local.entities.ProductOutEntity
import com.capstone.warungpintar.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteProductViewModel @Inject constructor(private val productRepository: ProductRepository) :
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
        viewModelScope.launch {
            productOutState.value = ResultState.Loading
            try {
                if (selectedProduct != null) {
                    val productOut = ProductOutEntity(
                        barangId = selectedProduct!!.id,
                        tanggalKeluar = tglKeluar,
                        jumlah = jumlah,
                        hargaJual = hargaJual
                    )
                    val res = productRepository.insertProductOut(productOut)

                    if (res > 0) {
                        productOutState.value =
                            ResultState.Success("Berhasil menambah barang keluar")
                    } else {
                        productOutState.value = ResultState.Error("Gagal menambah barang keluar")
                    }
                }
            } catch (e: Exception) {
                productOutState.value = ResultState.Error(e.message.toString())
            }
        }
    }
}