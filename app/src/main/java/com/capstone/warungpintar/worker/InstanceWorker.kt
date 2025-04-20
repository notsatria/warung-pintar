package com.capstone.warungpintar.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.capstone.warungpintar.data.local.entities.NotificationType
import java.util.concurrent.TimeUnit

object InstanceWorker {

    fun startDatabaseWorker(context: Context, workerType: String) {
        if (workerType == NotificationType.LOW_STOCK.value) {
            val workRequest = OneTimeWorkRequestBuilder<DatabaseWorker>()
                .setInputData(
                    workDataOf(
                        DatabaseWorker.WORKER_TYPE to NotificationType.LOW_STOCK.value
                    )
                )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "low_stock_product_worker",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        } else {
            val periodicRequest = PeriodicWorkRequestBuilder<DatabaseWorker>(1, TimeUnit.DAYS)
                .setInputData(
                    workDataOf(DatabaseWorker.WORKER_TYPE to NotificationType.EXPIRED_STOCK.value)
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "expired_product_worker",
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicRequest
            )
        }
    }

    fun startNotificationWorker(context: Context) {
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "notification_worker",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}