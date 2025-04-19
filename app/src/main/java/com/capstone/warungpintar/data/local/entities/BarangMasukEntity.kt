package com.capstone.warungpintar.data.local.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey

@Entity(tableName = "barang_masuk")
data class BarangMasukEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val barangId: Int,
    val tanggalMasuk: String, // format yyyy-MM-dd
    val jumlah: Int
)

@Dao
interface BarangMasukDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: BarangMasukEntity)
}
