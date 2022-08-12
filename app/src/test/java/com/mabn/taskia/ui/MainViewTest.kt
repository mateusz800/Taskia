package com.mabn.taskia.ui

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class MainViewTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @Test
    fun `show form on + button click`() {
        composeTestRule.onNodeWithTag("add_task_button").performClick()
        composeTestRule.onNodeWithTag("task_form").assertIsDisplayed()
    }

    @Test
    fun `title input is focused just after + button click`() {
        composeTestRule.onNodeWithTag("add_task_button").performClick()
        composeTestRule.onNodeWithTag("title_input").assertIsFocused()
    }




}