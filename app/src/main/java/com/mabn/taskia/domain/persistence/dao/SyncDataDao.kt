package com.mabn.taskia.domain.persistence.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mabn.taskia.domain.model.SyncData


@Dao
interface SyncDataDao {
    @Query("SELECT * FROM SyncData")
    fun getAll(): List<SyncData>
    @Insert
    fun insert(operation: SyncData):Long

    @Delete
    fun delete(operation: SyncData)
}