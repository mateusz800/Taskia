package com.example.doit.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0 ,
    var title: String,
    var status: Boolean = false,
    var description: String? = null,
    var parentId: Long? = null,
    var order: Int? = id.toInt()
)