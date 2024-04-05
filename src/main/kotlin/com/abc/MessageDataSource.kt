package com.abc

import com.abc.data.model.Message

interface MessageDataSource {

    suspend fun getAllMessages():List<Message>
    suspend fun insertMessage(message:Message)
}