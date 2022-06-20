package com.mabn.taskia.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mabn.taskia.ui.settings.connectedAccounts.ConnectedAccountsActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingsView(startConnectedAccountsActivity = { startConnectedAccountsActivity() })
        }
    }

    private fun startConnectedAccountsActivity() {
        startActivity(Intent(this, ConnectedAccountsActivity::class.java))
    }


}