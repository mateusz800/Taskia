package com.mabn.taskia.ui.common.drawer

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.mabn.taskia.domain.ContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val contextProvider: ContextProvider
) : ViewModel() {

    fun shareApp() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            //TODO: App promote text with link to google play
            putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        contextProvider.startActivity(shareIntent)
    }
}