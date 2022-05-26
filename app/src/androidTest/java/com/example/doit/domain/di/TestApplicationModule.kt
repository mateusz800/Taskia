package com.example.doit.domain.di

import android.content.Context
import androidx.room.Room
import com.example.doit.domain.ResourcesProvider
import com.example.doit.domain.persistence.AppDatabase
import com.example.doit.domain.persistence.dao.TaskDao
import com.example.doit.domain.persistence.repository.MessageRepository
import com.example.doit.domain.persistence.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [ApplicationModule::class])
class TestApplicationModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
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
    fun provideResourceProvider(@ApplicationContext context: Context): ResourcesProvider {
        return ResourcesProvider(context)
    }
}
