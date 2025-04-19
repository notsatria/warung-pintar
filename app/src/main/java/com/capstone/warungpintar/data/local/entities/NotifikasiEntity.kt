package com.capstone.warungpintar.data.local.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey

@Entity(tableName = "notifikasi")
data class NotifikasiEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val isi: String,
    val tanggal: String,
    val isRead: Boolean = false
)

@Dao
interface NotifikasiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: NotifikasiEntity)
}