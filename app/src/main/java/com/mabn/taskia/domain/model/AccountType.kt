package com.mabn.taskia.domain.model

import com.mabn.taskia.R

enum class AccountType(val title: String, val description:Int? = null) {
    UNKNOWN("unknown"),
    GOOGLE("Google", description = R.string.synchronize_with_google_tasks)
}