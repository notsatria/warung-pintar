package com.capstone.warungpintar.data.local.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.capstone.warungpintar.data.model.Product
import com.capstone.warungpintar.utils.toStrDate
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
    val expired: Long,
    val hargaBeli: Int,
    val hargaJual: Int,
    val imagePath: String,
    val kategoriId: Int,
    val tanggalMasuk: Long,
) {
    fun toProduct(): Product {
        return Product(
            productId = id,
            productName = nama,
            entryDate = tanggalMasuk.toStrDate(),
            productCategory = "",
            productQuantity = jumlah,
            lowStock = lowStock,
            codeStock = "",
            purchasePrice = hargaBeli,
            sellingPrice = hargaJual,
            imageUrl = imagePath,
            expiredDate = expired.toStrDate()
        )
    }
}


@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: ProductEntity): Long

    @Query("SELECT * FROM product")
    fun getAllProduct(): Flow<List<ProductEntity>>

    @Transaction
    @Query("SELECT * FROM product")
    fun getAllProductWithCategory(): Flow<List<ProductWithCategory>>

    @Transaction
    @Query("SELECT * FROM product WHERE id = :id")
    fun getProductWithCategoryById(id: Int): ProductWithCategory

    @Query("SELECT COUNT(id) FROM product")
    fun getProductLength(): Flow<Int>

    @Query("SELECT COUNT(*) FROM product WHERE jumlah <= lowStock")
    fun getProductsWithLowStockLength(): Flow<Int>

    @Query("""
        SELECT p.nama, MAX(po.tanggalKeluar) as tanggalKeluar
        FROM product p
        JOIN product_out po ON po.barangId = p.id
        WHERE p.jumlah <= p.lowStock
        GROUP BY p.id
        """)
    suspend fun getProductsWithLowStock(): List<ProductWithProductOutRaw>

    @Update
    suspend fun update(product: ProductEntity)

    @Query(
        """
            SELECT nama
            FROM product
            WHERE expired <= :today
        """
    )
    suspend fun getExpiredProducts(today: Long): List<String>

    @Query("SELECT * FROM product WHERE LOWER(nama) LIKE '%' || LOWER(:name) || '%'")
    suspend fun getProductByName(name: String): ProductEntity
}