package com.example.doit.domain.persistence.repository

import androidx.test.filters.SmallTest
import com.example.doit.domain.model.Task
import com.example.doit.domain.persistence.AppDatabase
import com.example.doit.domain.persistence.dao.TaskDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@SmallTest
@HiltAndroidTest
class TaskRepositoryTest {
    @Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: AppDatabase

    @Inject
    private lateinit var taskDao: TaskDao

    @Before
    fun createDb() {
        hiltRule.inject()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeTaskAndReadItInList() {
        val task = Task(title="Task no 1")
        val taskId:Int = taskDao.insertAll(task)[0];
        val subtask = Task(title = "subtask", parentId = taskId)
        taskDao.insertAll(subtask)
        val result = taskDao.getAll()
        assertEquals(result.keys.size, 1)
    }
}