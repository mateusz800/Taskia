package com.mabn.taskia.ui.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RestrictTo
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(RestrictTo.Scope(CalendarScopes.CALENDAR))
            .requestServerAuthCode("818157017237-hidphgq8htcqmc03bjn8r112buvbtdmn.apps.googleusercontent.com")
            .requestEmail()
            .build()
        setContent{
            SettingsView()
        }
    }
}