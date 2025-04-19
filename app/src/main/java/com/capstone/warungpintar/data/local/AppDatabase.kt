package com.capstone.warungpintar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.capstone.warungpintar.data.local.entities.BarangDao
import com.capstone.warungpintar.data.local.entities.BarangEntity
import com.capstone.warungpintar.data.local.entities.BarangKeluarDao
import com.capstone.warungpintar.data.local.entities.BarangKeluarEntity
import com.capstone.warungpintar.data.local.entities.BarangMasukDao
import com.capstone.warungpintar.data.local.entities.BarangMasukEntity
import com.capstone.warungpintar.data.local.entities.KategoriDao
import com.capstone.warungpintar.data.local.entities.KategoriEntity
import com.capstone.warungpintar.data.local.entities.NotifikasiDao
import com.capstone.warungpintar.data.local.entities.NotifikasiEntity
import com.capstone.warungpintar.data.local.entities.UserDao
import com.capstone.warungpintar.data.local.entities.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        KategoriEntity::class,
        BarangEntity::class,
        BarangMasukEntity::class,
        BarangKeluarEntity::class,
        NotifikasiEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun kategoriDao(): KategoriDao
    abstract fun barangDao(): BarangDao
    abstract fun barangMasukDao(): BarangMasukDao
    abstract fun barangKeluarDao(): BarangKeluarDao
    abstract fun notifikasiDao(): NotifikasiDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "warung_app_db"
                )
                    .addCallback(prepopulateCallback)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val prepopulateCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.kategoriDao()?.insertAll(
                        listOf(
                            KategoriEntity(namaKategori = "Makanan"),
                            KategoriEntity(namaKategori = "Minuman"),
                            KategoriEntity(namaKategori = "Alat Tulis"),
                            KategoriEntity(namaKategori = "Kebutuhan Rumah"),
                            KategoriEntity(namaKategori = "Kebersihan"),
                            KategoriEntity(namaKategori = "Perawatan Diri"),
                            KategoriEntity(namaKategori = "Elektronik"),
                            KategoriEntity(namaKategori = "Snack"),
                            KategoriEntity(namaKategori = "Bumbu Dapur"),
                            KategoriEntity(namaKategori = "Sembako"),
                            KategoriEntity(namaKategori = "Frozen Food"),
                            KategoriEntity(namaKategori = "Rokok"),
                            KategoriEntity(namaKategori = "Susu & Olahan"),
                            KategoriEntity(namaKategori = "Mie Instan"),
                            KategoriEntity(namaKategori = "Minyak Goreng"),
                            KategoriEntity(namaKategori = "Gas Elpiji"),
                            KategoriEntity(namaKategori = "Air Mineral"),
                            KategoriEntity(namaKategori = "Obat-obatan"),
                            KategoriEntity(namaKategori = "Peralatan Mandi"),
                            KategoriEntity(namaKategori = "Peralatan Masak")
                        )
                    )
                }
            }
        }
    }
}
