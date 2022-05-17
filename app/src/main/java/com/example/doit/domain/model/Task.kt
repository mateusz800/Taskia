package com.example.doit.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0 ,
    val title: String,
    val status: Boolean = false,
    val description: String? = null,
    val parentId: Int? = null,
    val order: Int? = id
)