package com.mabn.taskia.domain.di

import android.content.Context
import androidx.room.Room
import com.mabn.taskia.domain.persistence.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "taskia")
            .fallbackToDestructiveMigration()
            .fallbackToDestructiveMigrationFrom(1)
            .build()
    }
}