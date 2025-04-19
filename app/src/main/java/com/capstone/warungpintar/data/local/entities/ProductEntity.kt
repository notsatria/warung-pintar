package com.capstone.warungpintar.data.local.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "product",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["kategoriId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val jumlah: Int,
    val lowStock: Int,
    val expired: String,
    val hargaBeli: Int,
    val hargaJual: Int,
    val imagePath: String?,
    val kategoriId: Int,
    val tanggalMasuk: String
)


@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: ProductEntity): Long

    @Query("SELECT * FROM product")
    fun getAllProduct(): Flow<List<ProductEntity>>
}