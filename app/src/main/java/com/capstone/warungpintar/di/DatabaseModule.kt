package com.capstone.warungpintar.di

import android.content.Context
import com.capstone.warungpintar.data.local.AppDatabase
import com.capstone.warungpintar.data.local.entities.BarangDao
import com.capstone.warungpintar.data.local.entities.BarangKeluarDao
import com.capstone.warungpintar.data.local.entities.BarangMasukDao
import com.capstone.warungpintar.data.local.entities.NotifikasiDao
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
    fun provideBarangDao(db: AppDatabase): BarangDao = db.barangDao()

    @Provides
    fun provideBarangMasukDao(db: AppDatabase): BarangMasukDao = db.barangMasukDao()

    @Provides
    fun provideBarangKeluarDao(db: AppDatabase): BarangKeluarDao = db.barangKeluarDao()

    @Provides
    fun provideNotifikasiDao(db: AppDatabase): NotifikasiDao = db.notifikasiDao()
}