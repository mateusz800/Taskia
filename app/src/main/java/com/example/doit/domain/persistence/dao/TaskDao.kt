package com.example.doit.domain.persistence.dao

import androidx.room.*
import com.example.doit.domain.model.Task
import com.example.doit.domain.model.TaskAndSubtasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    fun insertAll(vararg tasks: Task)

    @Update
    fun updateUsers(vararg tasks: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * FROM Task  ")
    fun getAll(): Flow<List<TaskAndSubtasks>>
}