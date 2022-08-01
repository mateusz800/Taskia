package com.mabn.taskia.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
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
    fun noTasksViewVisibleIfNoTasks() {
        composeTestRule.onNodeWithTag("noTasks", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun backPress() {
        composeTestRule.onNodeWithText("Upcoming").performClick()
        pressBackUnconditionally()
        assertTrue(composeTestRule.activity.isDestroyed)
    }

    @Test
    fun addNewTaskTest() {
        composeTestRule.onNodeWithTag("add_task_button").performClick()
        composeTestRule.onNodeWithTag("task_form").assertIsDisplayed()
        composeTestRule.onNodeWithTag("title_input").performTextInput("Sample task")
        composeTestRule.onNodeWithTag("save_task_button").performClick()
        composeTestRule.onNodeWithTag("task_form").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Sample task")
            .assertIsDisplayed()

    }

    @Test
    fun removeTaskOnSwipeRight() {
        val title = "test task"
        runBlocking {
            taskRepository.insertAll(
                Task(
                    title = title,
                    completionTime = LocalDate.now().atStartOfDay()
                )
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText(title).performTouchInput { swipeRight() }
        composeTestRule.onNodeWithText(title).assertDoesNotExist()
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