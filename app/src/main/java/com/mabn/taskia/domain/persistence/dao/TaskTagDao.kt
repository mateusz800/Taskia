package com.mabn.taskia.domain.persistence.dao

import androidx.room.*
import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.domain.model.TaskTag

@Dao
interface TaskTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg items: TaskTag): List<Long>

    @Query("SELECT * FROM Tag WHERE id IN (SELECT tagId FROM TASKTAG WHERE taskId = :id)")
    fun findTagsByTaskId(id: Long): List<Tag>

    @Query("SELECT * FROM TaskTag WHERE taskId=:taskId AND tagId=:tagId")
    fun find(taskId: Long, tagId: Long): TaskTag

    @Query("Select * FROM TASKTAG where tagId=:tagId AND taskId IN (SELECT id FROM Task)")
    fun findByTagId(tagId: Long): TaskTag?

    @Delete
    fun delete(taskTag: TaskTag)
}