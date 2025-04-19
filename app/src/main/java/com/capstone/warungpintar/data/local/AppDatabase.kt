package com.capstone.warungpintar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.capstone.warungpintar.data.local.entities.ProductDao
import com.capstone.warungpintar.data.local.entities.ProductEntity
import com.capstone.warungpintar.data.local.entities.ProductOutDao
import com.capstone.warungpintar.data.local.entities.ProductOutEntity
import com.capstone.warungpintar.data.local.entities.ProductInDao
import com.capstone.warungpintar.data.local.entities.ProductInEntity
import com.capstone.warungpintar.data.local.entities.CategoryDao
import com.capstone.warungpintar.data.local.entities.CategoryEntity
import com.capstone.warungpintar.data.local.entities.NotificationDao
import com.capstone.warungpintar.data.local.entities.NotificationEntity
import com.capstone.warungpintar.data.local.entities.UserDao
import com.capstone.warungpintar.data.local.entities.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        CategoryEntity::class,
        ProductEntity::class,
        ProductInEntity::class,
        ProductOutEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun kategoriDao(): CategoryDao
    abstract fun barangDao(): ProductDao
    abstract fun barangMasukDao(): ProductInDao
    abstract fun barangKeluarDao(): ProductOutDao
    abstract fun notifikasiDao(): NotificationDao

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
                            CategoryEntity(namaKategori = "Facial Treatments"),
                            CategoryEntity(namaKategori = "Eye Treatments"),
                            CategoryEntity(namaKategori = "Sun Protection"),
                            CategoryEntity(namaKategori = "Face Mask"),
                            CategoryEntity(namaKategori = "Moisturizer"),
                            CategoryEntity(namaKategori = "Cleanser"),
                        )
                    )
                }
            }
        }
    }
}
