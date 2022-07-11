package com.mabn.taskia.domain.di

import android.content.Context
import androidx.room.Room
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.mabn.taskia.domain.network.google.tasks.GoogleTasksApiClient
import com.mabn.taskia.domain.network.google.tasks.GoogleTasksSynchronizer
import com.mabn.taskia.domain.persistence.AppDatabase
import com.mabn.taskia.domain.persistence.dao.*
import com.mabn.taskia.domain.persistence.repository.*
import com.mabn.taskia.domain.util.ContextProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "taskia")
            .fallbackToDestructiveMigration()
            .fallbackToDestructiveMigrationFrom(1)
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(db: AppDatabase): TaskDao {
        return db.taskDao
    }

    @Provides
    @Singleton
    fun provideSyncDataDao(db: AppDatabase): SyncDataDao {
        return db.syncDataDao
    }

    @Provides
    @Singleton
    fun provideConnectedAccountDao(db: AppDatabase): ConnectedAccountDao {
        return db.connectedAccountDao
    }

    @Provides
    @Singleton
    fun provideTagDao(db: AppDatabase): TagDao {
        return db.tagDao
    }

    @Provides
    @Singleton
    fun provideTaskTagDao(db: AppDatabase): TaskTagDao {
        return db.taskTagDao
    }

    @Provides
    @Singleton
    fun provideSyncDataRepository(syncDataDao: SyncDataDao): SyncDataRepository {
        return SyncDataRepository(syncDataDao)
    }

    @Provides
    @Singleton
    fun provideTaskTagRepository(taskTagDao: TaskTagDao): TaskTagRepository {
        return TaskTagRepository(taskTagDao)
    }

    @Provides
    @Singleton
    fun provideTagRepository(tagDao: TagDao, taskTagDao: TaskTagDao): TagRepository {
        return TagRepository(tagDao, taskTagDao)
    }

    @Provides
    @Singleton
    fun provideConnectedAccountRepository(connectedAccountDao: ConnectedAccountDao): ConnectedAccountRepository {
        return ConnectedAccountRepository(connectedAccountDao)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        taskDao: TaskDao,
    ): TaskRepository {
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

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
            .protocols(listOf(Protocol.HTTP_1_1))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)

    }

    @Provides
    @Singleton
    fun provideGoogleTasksSynchronizer(
        connectedAccountRepository: ConnectedAccountRepository,
        googleSignInClient: GoogleSignInClient,
        googleTasksApiClient: GoogleTasksApiClient,
        contextProvider: ContextProvider,
        taskRepository: TaskRepository
    ): GoogleTasksSynchronizer {
        return GoogleTasksSynchronizer(
            connectedAccountRepository,
            googleSignInClient,
            googleTasksApiClient,
            contextProvider,
            taskRepository
        )

    }

}