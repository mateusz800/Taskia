package com.mabn.taskia.domain.di

import android.content.Context
import androidx.room.Room
import com.mabn.taskia.domain.persistence.AppDatabase
import com.mabn.taskia.domain.persistence.dao.ConnectedAccountDao
import com.mabn.taskia.domain.persistence.dao.TaskDao
import com.mabn.taskia.domain.persistence.repository.ConnectedAccountRepository
import com.mabn.taskia.domain.persistence.repository.MessageRepository
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import com.mabn.taskia.domain.util.ContextProvider
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
        return Room.databaseBuilder(context, AppDatabase::class.java, "taskia")
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(db: AppDatabase): TaskDao {
        return db.taskDao
    }

    @Provides
    @Singleton
    fun provideConnectedAccountDao(db: AppDatabase): ConnectedAccountDao {
        return db.connectedAccountDao
    }

    @Provides
    @Singleton
    fun provideConnectedAccountRepository(connectedAccountDao: ConnectedAccountDao): ConnectedAccountRepository {
        return ConnectedAccountRepository(connectedAccountDao)
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