package com.mabn.taskia.domain.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.persistence.dao.TaskDao
import com.mabn.taskia.domain.util.LocalDateTimeConverter

@Database(entities = [Task::class], version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
}