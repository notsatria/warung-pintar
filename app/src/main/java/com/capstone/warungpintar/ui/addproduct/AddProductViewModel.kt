package com.capstone.warungpintar.ui.addproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.local.entities.CategoryEntity
import com.capstone.warungpintar.data.local.entities.ProductEntity
import com.capstone.warungpintar.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    val resultUpload = MutableLiveData<ResultState<String>>()

    var categories = emptyList<CategoryEntity>()

    var selectedCategory: CategoryEntity? = null

    private var _resultOCR: MutableLiveData<ResultState<String>> = MutableLiveData()
    val resultOCR: LiveData<ResultState<String>> get() = _resultOCR

    init {
        getCategories()
    }

    fun getCategories() {
        viewModelScope.launch {
            productRepository.getCategories().collect { result ->
                categories = result
            }
        }
    }

    fun insertProductIn(
        namaBarang: String,
        tglMasuk: String,
        jmlMasuk: Int,
        stokRendah: Int,
        expiredDate: String,
        hargaBeli: Int,
        hargaJual: Int,
        imagePath: String
    ) {
        val product = ProductEntity(
            nama = namaBarang,
            lowStock = stokRendah,
            jumlah = jmlMasuk,
            expired = expiredDate,
            hargaBeli = hargaBeli,
            hargaJual = hargaJual,
            imagePath = imagePath,
            tanggalMasuk = tglMasuk,
            kategoriId = selectedCategory!!.id
        )

        viewModelScope.launch {
            try {
                resultUpload.value = ResultState.Loading
                productRepository.insertProduct(product)
                resultUpload.value = ResultState.Success("Berhasil menambahkan barang")
            } catch (e: Exception) {
                resultUpload.value = ResultState.Error(e.message.toString())
            }
        }
    }

    fun getAllProducts() =  productRepository.getProducts()

    fun performOCR(imageFile: File) {
        viewModelScope.launch {
//            productRepositoryOld.getExpiredFromOCR(imageFile).collect { result ->
//                _resultOCR.value = result
//            }
        }
    }
}