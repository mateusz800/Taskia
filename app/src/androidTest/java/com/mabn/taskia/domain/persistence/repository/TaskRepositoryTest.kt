package com.mabn.taskia.domain.persistence.repository

import androidx.test.filters.SmallTest
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.persistence.AppDatabase
import com.mabn.taskia.domain.persistence.dao.TaskDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@SmallTest
@HiltAndroidTest
class TaskRepositoryTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: AppDatabase

    @Inject
    lateinit var taskDao: TaskDao

    @Before
    fun createDb() {
        hiltRule.inject()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeTaskAndReadItInList() = runBlocking {
        val task = Task(title = "Task no 1")
        val taskId: Long = taskDao.insertAll(task)[0]
        val subtask = Task(title = "subtask", parentId = taskId)
        taskDao.insertAll(subtask)
        val result = taskDao.getAll().first()
        assertEquals(result.size, 1)
    }
}