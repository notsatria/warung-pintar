package com.capstone.warungpintar.data.local.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "notification")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val isi: String,
    val tanggal: Long,
    val tipe: String,
    val isSent: Boolean = false
)

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: NotificationEntity)

    @Query("SELECT * FROM notification ORDER BY tanggal DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("UPDATE notification SET isSent = 1 WHERE id = :id")
    suspend fun markAsSent(id: Int)

    @Query("SELECT * FROM notification WHERE isSent = 0")
    suspend fun getUnsentNotifications(): List<NotificationEntity>
}

enum class NotificationType(val value: String) {
    LOW_STOCK("Low Stock"), EXPIRED_STOCK("Expired Stock")
}