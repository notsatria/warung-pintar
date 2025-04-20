package com.capstone.warungpintar.data.repository

import android.content.Context
import com.capstone.warungpintar.data.local.entities.NotificationDao
import com.capstone.warungpintar.data.local.entities.NotificationEntity
import com.capstone.warungpintar.worker.InstanceWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao,
    @ApplicationContext private val context: Context
) {

    suspend fun insertNotification(notification: NotificationEntity) {
        notificationDao.insert(notification)
        InstanceWorker.startNotificationWorker(context)
    }

    fun getNotifications() =
        notificationDao.getAllNotifications()

}