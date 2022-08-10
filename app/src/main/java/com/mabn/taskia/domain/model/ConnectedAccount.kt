package com.mabn.taskia.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(
    indices = [Index(
        value = ["type", "token"],
        unique = true
    )]
)
@Parcelize
data class ConnectedAccount(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: AccountType,
    val userIdentifier: String,
    var token: String? = null,
    var refreshToken: String? = null,
    var data: String? = null
):Parcelable



