package com.example.doit.domain.persistence.repository

import com.example.doit.domain.model.Task
import com.example.doit.domain.model.TaskAndSubtasks
import com.example.doit.domain.persistence.dao.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.LinkedHashMap

class TaskRepository(
    private val taskDao: TaskDao
) {

    suspend fun getAll(): Flow<List<TaskAndSubtasks>> {
        return withContext(Dispatchers.IO) {
            taskDao.getAll()
        }
    }

    fun insertAll(vararg tasks: Task) {
        taskDao.insertAll(*tasks)
    }
}