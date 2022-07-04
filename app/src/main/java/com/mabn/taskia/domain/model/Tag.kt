package com.mabn.taskia.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(
        value = ["value"],
        unique = true
    )]
)
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val value: String
)
