package com.capstone.warungpintar.data.local.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "kategori_barang")
data class KategoriEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val namaKategori: String
)

@Dao
interface KategoriDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(kategoriList: List<KategoriEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kategori: KategoriEntity)

    @Query("SELECT * FROM kategori_barang ORDER BY namaKategori ASC")
    fun getAllKategori(): Flow<List<KategoriEntity>>

    @Query("DELETE FROM kategori_barang")
    suspend fun deleteAll()
}
