package com.mabn.taskia.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.mabn.taskia.domain.util.keyboard.AppKeyboardFocusManager
import com.mabn.taskia.domain.util.keyboard.KeyboardHeightProvider
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private val keyboardHeightProvider by lazy {
        KeyboardHeightProvider(
            this,
            windowManager, window.decorView, viewModel
        )
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { viewModel.currentState.value != MainViewState.Loaded }
        super.onCreate(savedInstanceState)
        setContent {
            AppKeyboardFocusManager()
            MainView(viewModel)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshData()
    }

    override fun onResume() {
        super.onResume()
        keyboardHeightProvider.start()
    }

    override fun onPause() {
        super.onPause()
        keyboardHeightProvider.dismiss()
    }

}
