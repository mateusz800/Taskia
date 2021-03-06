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
    val userIdentifier: String,
    var token: String? = null,
    var refreshToken: String? = null,
    var data: String? = null
)



