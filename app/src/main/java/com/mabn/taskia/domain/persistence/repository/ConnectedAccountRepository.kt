package com.mabn.taskia.domain.persistence.repository

import android.database.sqlite.SQLiteConstraintException
import com.mabn.taskia.domain.model.ConnectedAccount
import com.mabn.taskia.domain.persistence.dao.ConnectedAccountDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ConnectedAccountRepository(
    private val connectedAccountDao: ConnectedAccountDao
) {

    suspend fun getAll(): Flow<List<ConnectedAccount>> {
        return withContext(Dispatchers.IO) {
            connectedAccountDao.getAll()
        }
    }

    fun update(account: ConnectedAccount) {
        connectedAccountDao.update(account)
    }

    fun insert(connectedAccount: ConnectedAccount): Long {
        return try {
            connectedAccountDao.insert(connectedAccount)
        } catch (e: SQLiteConstraintException) {
            0
        }
    }

    fun delete(acc: ConnectedAccount) {
        connectedAccountDao.delete(acc)
    }
}