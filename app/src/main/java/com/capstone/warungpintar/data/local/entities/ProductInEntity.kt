package com.capstone.warungpintar.data.local.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.capstone.warungpintar.ui.history.HistoryItem
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "product_in")
data class ProductInEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val barangId: Int,
    val tanggalMasuk: Long,
    val jumlah: Int
)

@Dao
interface ProductInDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: ProductInEntity)

    @Query("SELECT COUNT(*) FROM product_in")
    fun getProductInLength(): Flow<Int>

    @Query("""
        SELECT 
            pi.id as historyId,
            p.nama as namaBarang,
            p.hargaBeli as harga,
            pi.jumlah as jumlah,
            pi.tanggalMasuk as tanggal,
            'MASUK' as tipe
        FROM product_in pi
        JOIN product p ON pi.barangId = p.id
        
        UNION ALL
        
        SELECT 
            po.id as historyId,
            p.nama as namaBarang,
            po.hargaJual as harga,
            po.jumlah as jumlah,
            po.tanggalKeluar as tanggal,
            'KELUAR' as tipe
        FROM product_out po
        JOIN product p ON po.barangId = p.id
        
        ORDER BY tanggal DESC
    """)
    fun getHistoryList(): Flow<List<HistoryItem>>

}
