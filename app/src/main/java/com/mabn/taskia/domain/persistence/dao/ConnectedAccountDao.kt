package com.mabn.taskia.domain.persistence.dao

import androidx.room.*
import com.mabn.taskia.domain.model.ConnectedAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface ConnectedAccountDao {
    @Insert
    fun insert(account: ConnectedAccount): Long

    @Delete
    fun delete(account: ConnectedAccount)

    @Update
    fun update(account:ConnectedAccount)

    @Query("SELECT * FROM ConnectedAccount")
    fun getAll(): Flow<List<ConnectedAccount>>
}