package com.mabn.taskia.domain.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mabn.taskia.domain.model.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("SELECT * FROM Tag ")
    fun getAll(): Flow<List<Tag>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(tag: Tag): Long

    @Query("SELECT * FROM Tag WHERE value=:value")
    fun getByValue(value: String): Tag
}