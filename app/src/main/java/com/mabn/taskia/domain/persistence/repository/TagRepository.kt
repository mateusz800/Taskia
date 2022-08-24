package com.mabn.taskia.domain.persistence.repository

import android.database.sqlite.SQLiteConstraintException
import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.domain.persistence.dao.TagDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TagRepository(
    private val tagDao: TagDao,
) {
    suspend fun getAll(): Flow<List<Tag>> {
        return withContext(Dispatchers.IO) {
            tagDao.getAll()
        }
    }

    fun insert(tag: Tag): Long {
        return try {
            tagDao.insert(tag)
        } catch (e: SQLiteConstraintException) {
            tagDao.getByValue(tag.value)?.id ?: 0
        }
    }
}