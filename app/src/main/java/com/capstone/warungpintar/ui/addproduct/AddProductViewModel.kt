package com.capstone.warungpintar.ui.addproduct

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.warungpintar.R
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.local.entities.CategoryEntity
import com.capstone.warungpintar.data.local.entities.NotificationEntity
import com.capstone.warungpintar.data.local.entities.NotificationType
import com.capstone.warungpintar.data.local.entities.ProductEntity
import com.capstone.warungpintar.data.local.entities.UserDao
import com.capstone.warungpintar.data.repository.NotificationRepository
import com.capstone.warungpintar.data.repository.ProductRepository
import com.capstone.warungpintar.preferences.UserPreferences
import com.capstone.warungpintar.utils.toMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val notificationRepository: NotificationRepository,
    private val userPreferences: UserPreferences,
    private val userDao: UserDao,
    @ApplicationContext private val context: Context
) :
    ViewModel() {

    val resultUpload = MutableLiveData<ResultState<String>>()

    var categories = emptyList<CategoryEntity>()

    var selectedCategory: CategoryEntity? = null

    var selectedProductName: String? = null

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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultUpload.postValue(ResultState.Loading)
                // update
                if (selectedProductName != null && selectedProductName == namaBarang) {
                    val productEntity = productRepository.getProductByName(selectedProductName!!)
                    productRepository.updateProduct(
                        productEntity.copy(
                            nama = namaBarang,
                            lowStock = stokRendah,
                            jumlah = jmlMasuk,
                            expired = expiredDate.toMillis(),
                            hargaBeli = hargaBeli,
                            hargaJual = hargaJual,
                            imagePath = imagePath,
                            tanggalMasuk = tglMasuk.toMillis(),
                            kategoriId = selectedCategory!!.id
                        )
                    )

                    doOnExpired(namaBarang, expiredDate)
                } else {
                    val product = ProductEntity(
                        nama = namaBarang,
                        lowStock = stokRendah,
                        jumlah = jmlMasuk,
                        expired = expiredDate.toMillis(),
                        hargaBeli = hargaBeli,
                        hargaJual = hargaJual,
                        imagePath = imagePath,
                        tanggalMasuk = tglMasuk.toMillis(),
                        kategoriId = selectedCategory!!.id
                    )

                    productRepository.insertProduct(product)

                    // kadaularsa
                    doOnExpired(namaBarang, expiredDate)
                }
                resultUpload.postValue(ResultState.Success("Berhasil menambahkan barang"))
            } catch (e: Exception) {
                resultUpload.postValue(ResultState.Error(e.message.toString()))
            }
        }
    }

    private suspend fun doOnExpired(productName: String, expiredDate: String) {
        val today = System.currentTimeMillis()
        val email = userPreferences.getUserEmail()
        val user = userDao.getUserByEmail(email.first())

        if (expiredDate.toMillis() <= today) {
            notificationRepository.insertNotification(
                NotificationEntity(
                    isi = context.getString(
                        R.string.desc_notification_expired_stock,
                        user.namaWarung,
                        productName
                    ),
                    tanggal = today,
                    tipe = NotificationType.EXPIRED_STOCK.toString(),
                    judul = NotificationType.EXPIRED_STOCK.value
                )
            )
        }
    }

    fun getAllProducts() = productRepository.getProducts()

    fun performOCR(imageFile: File) {
        viewModelScope.launch {
//            productRepositoryOld.getExpiredFromOCR(imageFile).collect { result ->
//                _resultOCR.value = result
//            }
        }
    }
}