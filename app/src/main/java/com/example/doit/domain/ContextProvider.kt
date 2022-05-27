package com.example.doit.domain

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK

class ContextProvider(private val context: Context) {
    fun getString(id: Int): String {
        return context.getString(id)
    }

    fun startActivity(intent: Intent) {
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}