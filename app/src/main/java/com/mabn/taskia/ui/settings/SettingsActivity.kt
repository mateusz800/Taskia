package com.mabn.taskia.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import com.mabn.taskia.R
import com.mabn.taskia.ui.common.base.ActivityWithActionBar
import com.mabn.taskia.ui.settings.connectedAccounts.ConnectedAccountsActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsActivity : ActivityWithActionBar() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.title = getString(R.string.settings)
        setContent {
            SettingsView(startConnectedAccountsActivity = { startConnectedAccountsActivity() })
        }
    }

    private fun startConnectedAccountsActivity() {
        startActivity(Intent(this, ConnectedAccountsActivity::class.java))
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }


}