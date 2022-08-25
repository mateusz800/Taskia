package com.mabn.taskia.ui.taskForm.editForm

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class EditFormActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<EditFormActivity>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var taskRepository: TaskRepository

    private lateinit var task: Task

    @Before
    fun setUp() {
        hiltRule.inject()
        task = Task(title = "test task")
        task = task.copy(id = taskRepository.insertAll(task)[0])
        composeTestRule.activity.setTask(task)
    }

    @Test
    fun addNewSubtaskTest() {
        val activity = composeTestRule.activity
        val subtaskName = "new test subtask"
        composeTestRule.onNodeWithText(activity.getString(R.string.add_subtask))
            .performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.new_subtask))
            .performTextInput(subtaskName)
        composeTestRule.onNodeWithText(activity.getString(R.string.save)).performClick()
        runBlocking {
            val subtasks = taskRepository.getSubtasks(task)
            assertEquals(subtasks[0].title, subtaskName)
            assertEquals(subtasks.size, 1)
        }
    }
}