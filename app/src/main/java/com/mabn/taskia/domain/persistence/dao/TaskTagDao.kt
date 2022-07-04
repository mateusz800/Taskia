package com.mabn.taskia.domain.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.domain.model.TaskTag

@Dao
interface TaskTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg items: TaskTag): List<Long>

    @Query("SELECT * FROM Tag WHERE id IN (SELECT tagId FROM TASKTAG WHERE taskId = :id)")
    fun findByTaskId(id: Long): List<Tag>
}