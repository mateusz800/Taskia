package com.mabn.taskia.domain.persistence.dao

import androidx.room.*
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.model.TaskAndSubtasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    fun insertAll(vararg tasks: Task): List<Long>

    @Update
    fun update(vararg tasks: Task)

    @Delete
    fun deleteAll(vararg task: Task): Int

    @Transaction
    @Query("SELECT * FROM Task WHERE parentId is null  ")
    fun getAll(): Flow<List<TaskAndSubtasks>>

    @Transaction
    @Query(
        "SELECT * FROM Task Where parentId is null AND " +
                "endDate BETWEEN :startTime AND :endTime AND status = 0"
    )
    fun getAll(startTime: Long, endTime: Long): Flow<List<TaskAndSubtasks>>

    @Transaction
    @Query(
        "SELECT * FROM Task WHERE parentId is null  AND status = 0 AND " +
                " endDate < :dateTime ORDER BY endDate ASC "
    )
    fun getUncompletedByTime(dateTime: Long): Flow<List<TaskAndSubtasks>>

    @Transaction
    @Query(
        "SELECT * FROM Task WHERE parentId is null  AND status = 0 AND " +
                " endDate >= :dateTime ORDER BY endDate ASC "
    )
    fun getUpcoming(dateTime: Long): Flow<List<TaskAndSubtasks>>

    @Transaction
    @Query("SELECT * FROM Task WHERE parentId is null AND endDate is null AND status = 0")
    fun getAllUnscheduled(): Flow<List<TaskAndSubtasks>>

    @Transaction
    @Query("SELECT * FROM Task WHERE parentId is null AND status = 1")
    fun getAllCompleted(): Flow<List<TaskAndSubtasks>>

    @Query("SELECT * FROM TASK WHERE id=:id")
    fun getById(id: Long): Task?

    @Query("SELECT * FROM Task WHERE parentId=:parentId")
    fun getSubtasks(parentId: Long): List<Task>
}