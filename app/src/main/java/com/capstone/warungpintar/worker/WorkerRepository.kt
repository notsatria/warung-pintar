package com.capstone.warungpintar.worker

import android.content.Context
import android.icu.util.Calendar
import com.capstone.warungpintar.R
import com.capstone.warungpintar.data.local.entities.NotificationDao
import com.capstone.warungpintar.data.local.entities.NotificationEntity
import com.capstone.warungpintar.data.local.entities.NotificationType
import com.capstone.warungpintar.data.local.entities.ProductDao
import com.capstone.warungpintar.data.local.entities.UserDao
import com.capstone.warungpintar.preferences.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerRepository @Inject constructor(
    val notificationDao: NotificationDao,
    val productDao: ProductDao,
    val userPreferences: UserPreferences,
    val userDao: UserDao,
    @ApplicationContext val context: Context
) {

    suspend fun getProductsWithLowStock() {
        val productWithLowStock = productDao.getProductsWithLowStock()
        val email = userPreferences.getUserEmail()
        val user = userDao.getUserByEmail(email.first())

        productWithLowStock.forEach { product ->
            // insert notification
            notificationDao.insert(
                NotificationEntity(
                    isi = context.getString(
                        R.string.desc_notification_low_stock,
                        user.namaWarung,
                        product.nama
                    ),
                    tanggal = product.tanggalKeluar,
                    tipe = NotificationType.LOW_STOCK.toString(),
                    judul = NotificationType.LOW_STOCK.value
                )
            )
        }
    }

    suspend fun getExpiredProducts() {
        val today = Calendar.getInstance()
        val expiredProducts = productDao.getExpiredProducts(today.timeInMillis)

        val email = userPreferences.getUserEmail()
        val user = userDao.getUserByEmail(email.first())

        expiredProducts.forEach { productName ->
            // insert notification
            notificationDao.insert(
                NotificationEntity(
                    isi = context.getString(
                        R.string.desc_notification_expired_stock,
                        user.namaWarung,
                        productName
                    ),
                    tanggal = today.timeInMillis,
                    tipe = NotificationType.EXPIRED_STOCK.toString(),
                    judul = NotificationType.EXPIRED_STOCK.value
                )
            )
        }
    }
}