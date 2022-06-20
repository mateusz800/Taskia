package com.mabn.taskia.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    indices = [Index(
        value = ["type", "token"],
        unique = true
    )]
)
data class ConnectedAccount(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: AccountType,
    val token: String,
    val userIdentifier: String
)