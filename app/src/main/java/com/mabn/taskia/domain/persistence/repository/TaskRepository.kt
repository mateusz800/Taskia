package com.mabn.taskia.domain.persistence.repository

import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.model.TaskAndSubtasks
import com.mabn.taskia.domain.persistence.dao.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneOffset

class TaskRepository(
    private val taskDao: TaskDao,
) {

    suspend fun getAllUncompleted(): Flow<List<TaskAndSubtasks>> {
        return withContext(Dispatchers.IO) {
            taskDao.getAll()
        }
    }


    suspend fun getAllUpcoming(): Flow<List<TaskAndSubtasks>> {
        return withContext(Dispatchers.IO) {
            taskDao.getAllUpcoming()
        }
    }

    fun getByGoogleId(googleId: String): Task? {
        return taskDao.getByGoogleId(googleId)
    }

    suspend fun getAllUnscheduled(): Flow<List<TaskAndSubtasks>> {
        return withContext(Dispatchers.IO) {
            taskDao.getAllUnscheduled()
        }
    }

    suspend fun getAllCompleted(): Flow<List<TaskAndSubtasks>> {
        return withContext(Dispatchers.IO) {
            taskDao.getAllCompleted()
        }
    }

    suspend fun getByDate(date: LocalDate): Flow<List<TaskAndSubtasks>> {
        return withContext(Dispatchers.IO) {
            taskDao.getByDate(date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
        }

    }

    suspend fun getTodayTasks(): Flow<List<TaskAndSubtasks>> {
        return withContext(Dispatchers.IO) {
            taskDao.getUncompletedByTime(
                LocalDate.now()
                    .atStartOfDay()
                    .plusDays(1)
                    .minusNanos(1)
                    .toInstant(ZoneOffset.UTC)
                    .toEpochMilli()
            )
        }
    }

    fun getById(id: Long): Task? {
        return taskDao.getById(id)
    }

    suspend fun getSubtasks(task: Task): List<Task> {
        return withContext(Dispatchers.IO) {
            taskDao.getSubtasks(task.id)
        }


    }

    fun insertAll(vararg tasks: Task): List<Long> {
        return taskDao.insertAll(*tasks)
    }

    fun delete(task: Task): Boolean {
        return taskDao.deleteAll(task) > 0
    }

    fun deleteAll(vararg task: Task) {
        taskDao.deleteAll(*task)
    }

    fun update(task: Task) {
        taskDao.update(task)
    }


}