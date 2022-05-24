package com.example.doit.domain

import android.content.Context

class ResourcesProvider(private val context: Context) {
    fun getString(id: Int): String {
        return context.getString(id)
    }
}