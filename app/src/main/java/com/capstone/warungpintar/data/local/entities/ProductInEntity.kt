package com.capstone.warungpintar.data.local.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT COUNT(*) FROM product_in")
    fun getProductInLength(): Flow<Int>
}
