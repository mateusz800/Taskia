package com.example.doit.domain.persistence.dao

import androidx.room.*
import com.example.doit.domain.model.Task
import com.example.doit.domain.model.TaskAndSubtasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    fun insertAll(vararg tasks: Task): List<Long>

    @Update
    fun update(vararg tasks: Task)

    @Delete
    fun deleteAll(vararg task: Task): Int


    @Query("SELECT * FROM Task WHERE parentId is null  ")
    fun getAll(): Flow<List<TaskAndSubtasks>>

    @Query("SELECT * FROM TASK WHERE id=:id")
    fun getById(id: Long): Task?

    @Query("SELECT * FROM Task WHERE parentId=:parentId")
    fun getSubtasks(parentId: Long): List<Task>
}