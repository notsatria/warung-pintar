package com.capstone.warungpintar.data.local.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey

@Entity(
    tableName = "barang",
    foreignKeys = [ForeignKey(
        entity = KategoriEntity::class,
        parentColumns = ["id"],
        childColumns = ["kategoriId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class BarangEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val jumlah: Int,
    val lowStock: Boolean,
    val expired: String?, // format tanggal: yyyy-MM-dd
    val hargaBeli: Int,
    val hargaJual: Int,
    val imagePath: String?, // path atau URI lokal
    val kategoriId: Int
)


@Dao
interface BarangDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: BarangEntity)
}