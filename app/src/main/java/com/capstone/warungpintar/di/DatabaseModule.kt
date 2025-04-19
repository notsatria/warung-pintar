package com.capstone.warungpintar.di

import android.content.Context
import com.capstone.warungpintar.data.local.AppDatabase
import com.capstone.warungpintar.data.local.entities.CategoryDao
import com.capstone.warungpintar.data.local.entities.ProductDao
import com.capstone.warungpintar.data.local.entities.ProductOutDao
import com.capstone.warungpintar.data.local.entities.ProductInDao
import com.capstone.warungpintar.data.local.entities.NotificationDao
import com.capstone.warungpintar.data.local.entities.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideBarangDao(db: AppDatabase): ProductDao = db.barangDao()

    @Provides
    fun provideBarangMasukDao(db: AppDatabase): ProductInDao = db.barangMasukDao()

    @Provides
    fun provideBarangKeluarDao(db: AppDatabase): ProductOutDao = db.barangKeluarDao()

    @Provides
    fun provideNotifikasiDao(db: AppDatabase): NotificationDao = db.notifikasiDao()

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.kategoriDao()
}