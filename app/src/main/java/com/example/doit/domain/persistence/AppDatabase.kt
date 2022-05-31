package com.example.doit.domain.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.doit.domain.model.Task
import com.example.doit.domain.persistence.dao.TaskDao
import com.example.doit.domain.util.LocalDateTimeConverter

@Database(entities = [Task::class], version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
}