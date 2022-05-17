package com.example.doit.domain.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.doit.domain.model.Task
import com.example.doit.domain.persistence.dao.TaskDao

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
}