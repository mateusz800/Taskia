package com.example.doit.domain.persistence.repository

import com.example.doit.domain.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class MessageRepository {
    private val _messages = Channel<Message>()

    suspend fun insertMessage(message: Message) {
        _messages.send(message)
    }

    suspend fun getAll(): Flow<Message> {
        return withContext(Dispatchers.IO) {
            _messages.consumeAsFlow()
        }
    }

}