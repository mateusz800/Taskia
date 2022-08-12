package com.mabn.taskia.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.NullPointerException
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var taskRepository: TaskRepository


    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun backPress() {
        composeTestRule.onNodeWithText("Calendar").performClick()
        pressBackUnconditionally()
        try {
            assertNull(composeTestRule.activity)
        } catch (e: NullPointerException) {
            assertTrue(true)
        }

    }

    @Test
    fun addNewTaskTest() {
        composeTestRule.onNodeWithTag("add_task_button").performClick()
        composeTestRule.onNodeWithTag("task_form").assertIsDisplayed()
        composeTestRule.onNodeWithTag("title_input")
            .performTextInput("Sample task")
        composeTestRule.onNodeWithTag("save_task_button").performClick()
        composeTestRule.onNodeWithTag("task_form").assertIsNotDisplayed()
        composeTestRule.onNode(
            hasText("Sample task")
                .and(hasParent(hasTestTag("task_list")))
        )
            .assertIsDisplayed()
    }
}