package com.mabn.taskia.domain.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.tasks.TasksScopes
import com.mabn.taskia.R
import com.mabn.taskia.domain.network.google.tasks.GoogleTasksApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiClientsModule {

    @Provides
    @Singleton
    fun provideGoogleSignInClient(@ApplicationContext context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(TasksScopes.TASKS), Scope(CalendarScopes.CALENDAR))
            .requestEmail()
            .requestIdToken(context.getString(R.string.google_client_id))
            .requestServerAuthCode(context.getString(R.string.google_client_id))
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    @Provides
    @Singleton
    fun provideGoogleTasksClient(retrofitBuilder: Retrofit.Builder): GoogleTasksApiClient {
        val retrofit = retrofitBuilder
            .baseUrl("https://tasks.googleapis.com/tasks/v1/")
            .build()
        return retrofit.create(GoogleTasksApiClient::class.java)
    }
}