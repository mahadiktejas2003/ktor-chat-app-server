package com.abc

import com.abc.data.model.Message

interface MessageDataSource {

    suspend fun getAllMessages():List<Message>          //coroutine
    suspend fun insertMessage(message:Message)
}


//go to implementation-> MessageDataSourseImplementation class