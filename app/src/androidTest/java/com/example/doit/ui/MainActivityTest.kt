package com.example.doit.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.doit.R
import com.example.doit.domain.model.Task
import com.example.doit.domain.persistence.repository.TaskRepository
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.runner.RunWith
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Test
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
        composeTestRule.waitForIdle()
    }

    @Test
    fun addNewTaskTest() {
        composeTestRule.onNodeWithTag("add_task_button").performClick()
        composeTestRule.onNodeWithTag("task_form").assertIsDisplayed()
        composeTestRule.onNodeWithTag("title_input").performTextInput("Sample task")
        composeTestRule.onNodeWithTag("save_task_button").performClick()
        composeTestRule.onNodeWithTag("task_form").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Sample task")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun removeTaskOnSwipeRight() {
        taskRepository.insertAll(Task(title = "test task"))
        composeTestRule.onNodeWithText("test task").performTouchInput { swipeRight() }
        composeTestRule.onNodeWithText("test task").assertDoesNotExist()
        // Check if snackbar with message is displayed
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.task_removed))
            .assertIsDisplayed()
    }

    /* Node with undo button not found
    @Test
    fun undoTaskRemoval() {
        taskRepository.insertAll(Task(title = "test task"))
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("test task").performTouchInput { swipeRight() }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().printToString()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.undo))
            .performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("test task").assertIsDisplayed()
    }
     */

}