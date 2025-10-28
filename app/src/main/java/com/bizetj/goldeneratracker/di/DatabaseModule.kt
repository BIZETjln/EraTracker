package com.bizetj.goldeneratracker.di

import android.content.Context
import androidx.room.Room
import com.bizetj.goldeneratracker.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "golden_era_tracker_db"
        ).build()
    }

    @Provides
    fun provideSeanceDao(database: AppDatabase) = database.seanceDao()

    @Provides
    fun provideExerciceDao(database: AppDatabase) = database.exerciceDao()

    @Provides
    fun provideHistoriqueSeanceDao(database: AppDatabase) = database.historiqueSeanceDao()
}