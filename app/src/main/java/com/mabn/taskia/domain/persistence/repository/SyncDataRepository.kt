package com.mabn.taskia.domain.persistence.repository

import com.mabn.taskia.domain.model.SyncData
import com.mabn.taskia.domain.persistence.dao.SyncDataDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncDataRepository(
    private val syncDataDao: SyncDataDao
) {
    suspend fun getAll(): List<SyncData> {
        return withContext(Dispatchers.IO) {
            syncDataDao.getAll()
        }
    }

    fun insert(operation: SyncData): Long {
        return syncDataDao.insert(operation)
    }

    fun delete(operation: SyncData) {
        syncDataDao.delete(operation)
    }
}