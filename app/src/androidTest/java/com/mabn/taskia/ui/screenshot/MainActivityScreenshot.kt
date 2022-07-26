package com.mabn.taskia.ui.screenshot

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import com.mabn.taskia.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import tools.fastlane.screengrab.FalconScreenshotStrategy
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.cleanstatusbar.CleanStatusBar
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

    /*
    companion object {
        @BeforeClass
        fun beforeAll() {
            CleanStatusBar.disable()
        }

        @AfterClass
        fun afterAll() {
            CleanStatusBar.enableWithDefaults()
        }
    }
     */


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
        //composeTestRule.waitForIdle()
    }

    @Test
    fun takeAddTaskScreenshot() {
        composeTestRule.onNodeWithTag("add_task_button").performClick()
        //composeTestRule.onNodeWithTag("title_input")
        //    .performTextInput(composeTestRule.activity.getString(R.string.call_alice))
        //composeTestRule.waitForIdle()
        Screengrab.screenshot("AddNewTask")
    }

    /*
    @Test
    fun takeEditTaskScreenshot() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.finish_presentation_file))
            .performClick()
        composeTestRule.waitForIdle()
        Screengrab.screenshot("EditTask")
    }
     */

    @Test
    fun takeListScreenshot() {

        //composeTestRule.waitUntil { hasText(composeTestRule.activity.getString(R.string.call_alice)).matches(is) }
        composeTestRule.waitForIdle()
        runBlocking { delay(5000) }
        Screengrab.screenshot("TaskList")
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        //Screengrab.screenshot("DarkMode")
    }
}