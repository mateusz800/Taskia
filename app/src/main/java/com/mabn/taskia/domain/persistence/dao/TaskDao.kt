package com.mabn.taskia.domain.persistence.dao

import androidx.room.*
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.model.TaskAndSubtasks
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

@Dao
interface TaskDao {
    @Insert
    fun insertAll(vararg tasks: Task): List<Long>

    @Update
    fun update(vararg tasks: Task)

    @Delete
    fun deleteAll(vararg task: Task): Int

    @Transaction
    @Query("SELECT * FROM Task WHERE parentId is null AND isRemoved = 0 AND status = 0 OR (status = 1 AND completionTime > :yesterdayDate) ORDER BY endDate ASC, startTime ASC ")
    fun getAll(
        yesterdayDate: LocalDateTime = LocalDate.now().atStartOfDay().minusNanos(1),
    ): Flow<List<TaskAndSubtasks>>

    @Transaction
    @Query("SELECT * FROM Task WHERE parentId is null and endDate is not null and endDate >= :startDateTime and status = 0 order by endDate asc, startTime asc")
    fun getAllUpcoming(
        startDateTime: LocalDateTime = LocalDate.now().atStartOfDay()
    ): Flow<List<TaskAndSubtasks>>

    @Transaction
    @Query(
        "SELECT * FROM Task Where parentId is null AND " +
                "endDate BETWEEN :startTime AND :endTime AND status = 0 ORDER BY endDate ASC , startTime ASC"
    )
    fun getAll(startTime: Long, endTime: Long): Flow<List<TaskAndSubtasks>>

    @Transaction
    @Query("SELECT * FROM Task WHERE endDate = :date OR (endDate is null AND status = 0)  ORDER BY startTime ASC")
    fun getByDateAndUnscheduled(date: Long): Flow<List<TaskAndSubtasks>>

    @Transaction
    @Query(
        "SELECT * FROM Task WHERE parentId is null  AND status = 0 AND " +
                " endDate < :dateTime ORDER BY endDate ASC, startTime ASC "
    )
    fun getUncompletedByTime(dateTime: Long): Flow<List<TaskAndSubtasks>>

    @Transaction
    @Query(
        "SELECT * FROM Task WHERE parentId is null  AND status = 0 AND " +
                " endDate >= :dateTime ORDER BY endDate ASC, startTime ASC "
    )
    fun getUpcoming(dateTime: Long): Flow<List<TaskAndSubtasks>>

    @Transaction
    @Query("SELECT * FROM Task WHERE parentId is null AND endDate is null AND status = 0")
    fun getAllUnscheduled(): Flow<List<TaskAndSubtasks>>

    @Transaction
    @Query("SELECT * FROM Task WHERE parentId is null AND status = 1 ORDER BY completionTime DESC")
    fun getAllCompleted(): Flow<List<TaskAndSubtasks>>

    @Query("SELECT * FROM TASK WHERE id=:id")
    fun getById(id: Long): Task?

    @Query("SELECT * FROM TASK WHERE googleId=:googleId")
    fun getByGoogleId(googleId: String): Task?

    @Query("SELECT * FROM Task WHERE parentId=:parentId")
    fun getSubtasks(parentId: Long): List<Task>


}