package com.capstone.warungpintar.data.local.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey

@Entity(tableName = "product_in")
data class ProductInEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val barangId: Int,
    val tanggalMasuk: String,
    val jumlah: Int
)

@Dao
interface ProductInDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: ProductInEntity)
}
