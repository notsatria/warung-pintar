package com.capstone.warungpintar.data.local.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey

@Entity(tableName = "product_out")
data class ProductOutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val barangId: Int,
    val tanggalKeluar: String,
    val jumlah: Int,
    val hargaJual: Int
)

@Dao
interface ProductOutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productOut: ProductOutEntity): Long
}
