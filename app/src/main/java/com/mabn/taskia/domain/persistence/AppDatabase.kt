package com.mabn.taskia.domain.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mabn.taskia.domain.model.*
import com.mabn.taskia.domain.persistence.dao.*
import com.mabn.taskia.domain.util.dbConverter.ConnectedAccountConverter
import com.mabn.taskia.domain.util.dbConverter.LocalDateTimeConverter

@Database(
    entities = [Task::class, ConnectedAccount::class, Tag::class, TaskTag::class, SyncData::class],
    version = 2,
    exportSchema = true,
    autoMigrations = []
)
@TypeConverters(LocalDateTimeConverter::class, ConnectedAccountConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val syncDataDao: SyncDataDao
    abstract val taskDao: TaskDao
    abstract val connectedAccountDao: ConnectedAccountDao
    abstract val tagDao: TagDao
    abstract val taskTagDao: TaskTagDao

}