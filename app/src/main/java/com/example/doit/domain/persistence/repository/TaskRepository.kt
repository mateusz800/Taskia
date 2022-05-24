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

    fun getById(id:Long):Task?{
        return taskDao.getById(id)
    }

    fun getSubtasks(task: Task): List<Task> {
        return taskDao.getSubtasks(task.id)

    }


    fun insertAll(vararg tasks: Task): List<Long> {
        return taskDao.insertAll(*tasks)
    }

    fun remove(task: Task):Boolean {
        return taskDao.delete(task) > 0
    }

    fun update(task: Task) {
        taskDao.update(task)
    }
}