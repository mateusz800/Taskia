package com.mabn.taskia.domain.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mabn.taskia.domain.model.ConnectedAccount
import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.model.TaskTag
import com.mabn.taskia.domain.persistence.dao.ConnectedAccountDao
import com.mabn.taskia.domain.persistence.dao.TagDao
import com.mabn.taskia.domain.persistence.dao.TaskDao
import com.mabn.taskia.domain.persistence.dao.TaskTagDao
import com.mabn.taskia.domain.util.dbConverter.ConnectedAccountConverter
import com.mabn.taskia.domain.util.dbConverter.LocalDateTimeConverter

@Database(
    entities = [Task::class, ConnectedAccount::class, Tag::class, TaskTag::class],
    version = 2,
    exportSchema = true,
    autoMigrations = []
)
@TypeConverters(LocalDateTimeConverter::class, ConnectedAccountConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
    abstract val connectedAccountDao: ConnectedAccountDao
    abstract val tagDao: TagDao
    abstract val taskTagDao: TaskTagDao

}