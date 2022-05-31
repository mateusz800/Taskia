package com.example.doit.domain.di

import android.content.Context
import androidx.room.Room
import com.example.doit.domain.ContextProvider
import com.example.doit.domain.persistence.AppDatabase
import com.example.doit.domain.persistence.dao.TaskDao
import com.example.doit.domain.persistence.repository.MessageRepository
import com.example.doit.domain.persistence.repository.TaskRepository
import com.example.doit.domain.util.LocalDateTimeConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "doit")
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(db: AppDatabase): TaskDao {
        return db.taskDao
    }

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepository(taskDao)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(): MessageRepository {
        return MessageRepository()
    }

    @Provides
    @Singleton
    fun provideResourceProvider(@ApplicationContext context: Context): ContextProvider {
        return ContextProvider(context)
    }
}