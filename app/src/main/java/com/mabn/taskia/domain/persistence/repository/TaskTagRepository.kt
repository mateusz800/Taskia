package com.mabn.taskia.domain.persistence.repository

import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.model.TaskTag
import com.mabn.taskia.domain.persistence.dao.TaskTagDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskTagRepository(
    private val taskTagDao: TaskTagDao,
) {
    fun insert(vararg items: TaskTag): List<Long> {
        return taskTagDao.insertAll(*items)
    }

    fun deleteTagFromTask(tag: Tag, task: Task) {
        val item = taskTagDao.find(taskId = task.id, tagId = tag.id)
        taskTagDao.delete(item)
    }

    suspend fun getTags(task: Task): List<Tag> {
        return withContext(Dispatchers.IO) {
            taskTagDao.findTagsByTaskId(task.id)
        }
    }
}