package com.example.doit.domain.persistence.repository

import com.example.doit.domain.model.Task
import com.example.doit.domain.model.TaskAndSubtasks
import com.example.doit.domain.persistence.dao.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TaskRepository(
    private val taskDao: TaskDao
) {

    suspend fun getAll(): Flow<List<TaskAndSubtasks>> {
        return withContext(Dispatchers.IO) {
            taskDao.getAll()
        }
    }


    fun insertAll(vararg tasks: Task):List<Long> {
        return taskDao.insertAll(*tasks)
    }

    fun remove(task: Task){
        taskDao.delete(task)
    }

    fun update(task:Task){
        taskDao.update(task)
    }
}