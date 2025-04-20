package com.capstone.warungpintar.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.capstone.warungpintar.R
import com.capstone.warungpintar.data.local.entities.NotificationDao
import com.capstone.warungpintar.data.local.entities.NotificationEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    val notificationDao: NotificationDao
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val unsentNotifications = notificationDao.getUnsentNotifications()

            Timber.d("Unsent notifications: $unsentNotifications")

            unsentNotifications.forEach { notif ->
                showNotification(notif)
                notificationDao.markAsSent(notif.id)
                Timber.d("Notification marked as sent: ${notif.id} ${notif.judul}")
            }

            return Result.success()
        } catch (e: Exception) {
            Timber.e("Error on notification worker: ${e.message}")
            return Result.retry()
        }
    }

    private fun showNotification(notification: NotificationEntity) {
        val channelId = "notification_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "WarungPintar Notifikasi",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notification.judul)
            .setSound(defaultSoundUri)
            .setContentText(notification.isi)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(notification.id, builder.build())
    }
}
