package com.capstone.warungpintar.data.local.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val namaKategori: String
)

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(kategoriList: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kategori: CategoryEntity)

    @Query("SELECT * FROM category ORDER BY namaKategori ASC")
    fun getAllKategori(): Flow<List<CategoryEntity>>

    @Query("DELETE FROM category")
    suspend fun deleteAll()
}
