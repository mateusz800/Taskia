package com.mabn.taskia.ui.screenshot

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import com.mabn.taskia.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tools.fastlane.screengrab.FalconScreenshotStrategy
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.locale.LocaleTestRule
import java.time.LocalDate
import javax.inject.Inject


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityScreenshot {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Rule
    @JvmField
    val localeTestRule = LocaleTestRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var taskRepository: TaskRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        taskRepository.insertAll(
            Task(
                title = composeTestRule.activity.getString(R.string.finish_presentation_file),
                endDate = LocalDate.now().atStartOfDay()
            ),
            Task(
                title = composeTestRule.activity.getString(R.string.buy_present),
                endDate = LocalDate.now().atStartOfDay()
            ),
            Task(
                title = composeTestRule.activity.getString(R.string.call_alice),
                endDate = LocalDate.now().atStartOfDay()
            )
        )
        Screengrab.setDefaultScreenshotStrategy(FalconScreenshotStrategy(composeTestRule.activity))
    }

    @Test
    fun takeListScreenshot() {
        composeTestRule.waitForIdle()
        Screengrab.screenshot("01_TaskList")
    }

    @Test
    fun takeCalendarScreenshot(){
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.calendar)).performClick()
        composeTestRule.waitForIdle()
        Screengrab.screenshot("02_Calendar")
    }

    @Test
    fun takeAddTaskScreenshot() {
        composeTestRule.onNodeWithTag("add_task_button").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("title_input")
            .performTextInput(composeTestRule.activity.getString(R.string.call_alice))
        composeTestRule.waitForIdle()
        Screengrab.screenshot("03_AddNewTask")
    }

    @Test
    fun takeEditTaskScreenshot() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.finish_presentation_file))
            .performClick()
        composeTestRule.waitForIdle()
        Screengrab.screenshot("04_EditTask")
    }
}