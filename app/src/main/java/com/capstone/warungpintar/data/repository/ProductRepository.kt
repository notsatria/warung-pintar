package com.capstone.warungpintar.data.repository

import android.content.Context
import com.capstone.warungpintar.R
import com.capstone.warungpintar.data.local.entities.CategoryDao
import com.capstone.warungpintar.data.local.entities.NotificationDao
import com.capstone.warungpintar.data.local.entities.NotificationEntity
import com.capstone.warungpintar.data.local.entities.NotificationType
import com.capstone.warungpintar.data.local.entities.ProductDao
import com.capstone.warungpintar.data.local.entities.ProductEntity
import com.capstone.warungpintar.data.local.entities.ProductInDao
import com.capstone.warungpintar.data.local.entities.ProductInEntity
import com.capstone.warungpintar.data.local.entities.ProductOutDao
import com.capstone.warungpintar.data.local.entities.ProductOutEntity
import com.capstone.warungpintar.data.local.entities.UserDao
import com.capstone.warungpintar.preferences.UserPreferences
import com.capstone.warungpintar.utils.toMillis
import com.capstone.warungpintar.worker.InstanceWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val productInDao: ProductInDao,
    private val productOutDao: ProductOutDao,
    private val categoryDao: CategoryDao,
    private val notificationDao: NotificationDao,
    @ApplicationContext private val context: Context,
    private val userPreferences: UserPreferences
) {

    suspend fun insertProduct(product: ProductEntity) {
        val productId = productDao.insert(product)
        productInDao.insert(
            ProductInEntity(
                barangId = productId.toInt(),
                tanggalMasuk = product.tanggalMasuk,
                jumlah = product.jumlah
            )
        )
    }

    suspend fun insertProductIn(productIn: ProductInEntity) =
        productInDao.insert(productIn)

    suspend fun insertProductOut(productOut: ProductOutEntity) = productOutDao.insert(productOut)

    fun getCategories() = categoryDao.getAllKategori()

    fun getAllProductWithCategory() = productDao.getAllProductWithCategory()

    fun getProducts() = productDao.getAllProduct()

    fun getProductLength() = productDao.getProductLength()

    fun getProductInLength() = productInDao.getProductInLength()

    fun getProductOutLength() = productOutDao.getProductOutLength()

    fun getProductsWithLowStockLength() = productDao.getProductsWithLowStockLength()

    fun getHistoryList() = productInDao.getHistoryList()

    fun getReportList() = productOutDao.getReportList()

    suspend fun updateProduct(
        product: ProductEntity,
        isOnProductOut: Boolean = false,
        tanggalKeluar: String? = null
    ) = withContext(Dispatchers.IO) {
        productDao.update(product)

        if (product.jumlah <= product.lowStock && isOnProductOut) {
            val warung = userPreferences.getUserWarung().first()
            // insert notification
            notificationDao.insert(
                NotificationEntity(
                    isi = context.getString(
                        R.string.desc_notification_low_stock,
                        warung,
                        product.nama
                    ),
                    tanggal = tanggalKeluar.toString().toMillis(),
                    tipe = NotificationType.LOW_STOCK.toString(),
                    judul = NotificationType.LOW_STOCK.value
                )
            )
            InstanceWorker.startNotificationWorker(context)
        }
    }

    suspend fun getProductByName(name: String) = productDao.getProductByName(name)
}