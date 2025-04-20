package com.capstone.warungpintar.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.capstone.warungpintar.R
import com.capstone.warungpintar.data.local.entities.NotificationDao
import com.capstone.warungpintar.data.local.entities.NotificationEntity
import com.capstone.warungpintar.data.local.entities.NotificationType
import com.capstone.warungpintar.data.local.entities.ProductDao
import com.capstone.warungpintar.data.local.entities.UserDao
import com.capstone.warungpintar.preferences.UserPreferences
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

@HiltWorker
class DatabaseWorker @AssistedInject constructor(
    val workerRepository: WorkerRepository,
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
) :
    CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        try {
            val workerType = inputData.getString(WORKER_TYPE) ?: NotificationType.LOW_STOCK.value

            return try {
                when (workerType) {
                    "low_stock" -> workerRepository.getProductsWithLowStock()
                    "expired" -> workerRepository.getExpiredProducts()
                }
                Result.success()
            } catch (e: Exception) {
                Timber.e("Error on database worker: ${e.message}")
                Result.retry()
            }
        } catch (e: Exception) {
            Timber.e("Error on database worker: ${e.message}")
            return Result.retry()
        }
    }

    companion object {
        const val WORKER_TYPE = "worker_type"
    }
}