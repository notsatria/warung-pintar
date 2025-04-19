package com.capstone.warungpintar.data.local.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.capstone.warungpintar.ui.report.ReportItem
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT COUNT(*) FROM product_out")
    fun getProductOutLength(): Flow<Int>

    @Query("""
        SELECT 
            po.id as id,
            p.nama as namaBarang,
            po.hargaJual as hargaJual,
            p.hargaBeli as hargaBeli,
            (po.hargaJual - p.hargaBeli) as total,
            po.jumlah as jumlah,
            po.tanggalKeluar as tanggalKeluar
        FROM product_out po
        JOIN product p ON po.barangId = p.id
        
        ORDER BY tanggalKeluar DESC

    """)
    fun getReportList(): Flow<List<ReportItem>>

}
